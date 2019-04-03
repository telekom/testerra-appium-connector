/* 
 * Created on 27.07.2016
 * 
 * Copyright(c) 2011 - 2016 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.driver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.mobile.MobileProperties;
import eu.tsystems.mms.tic.testframework.mobile.cloud.api.CloudApiClient;
import eu.tsystems.mms.tic.testframework.mobile.device.TestDevice;
import eu.tsystems.mms.tic.testframework.mobile.utils.TrackingClient;

/**
 * Send screenshots to live tracking service.
 * 
 * @author sepr
 */
public class ScreenshotTracker extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotTracker.class);
    private static ScreenshotTracker instance = null;
    private static TestDevice device = null;
    private static Path currentFile = null;
    private long lastSent;
    private boolean stopped = false;
    private TrackingClient client;
    /** Title currently shown by live tracking */
    private String title;

    /**
     * Default constructor.
     */
    public ScreenshotTracker(String trackingUrl) {
        client = new TrackingClient(trackingUrl);
        lastSent = System.currentTimeMillis();
        title = "";
        this.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (!stopped) {
                    client.stopTracking();
                    stopped = true;
                }
            }

            protected void finalize() throws Throwable {
                if (!stopped) {
                    client.stopTracking();
                    stopped = true;
                }
            }
        });
    }

    /**
     * Set test device to take screenshots from via cloud api.
     * 
     * @param testDevice Testdevice to set.
     */
    public static void setDevice(TestDevice testDevice) {
        device = testDevice;
    }

    /**
     * Start thread to use screenshots tracking service.
     * @return started instance or null if tracking is deactivated.
     */
    public static ScreenshotTracker startTracking() {
        if (instance == null && PropertyManager.getBooleanProperty(MobileProperties.MOBILE_SCREENSHOTTRACKER_ACTIVATED, false)) {
            String trackingUrl = PropertyManager.getProperty(MobileProperties.MOBILE_SCREENSHOTTRACKER_URL);
            if (trackingUrl == null) {
                LOGGER.error(String.format("Property %s not set. Could not start tracking.", MobileProperties.MOBILE_SCREENSHOTTRACKER_URL));
                return null;
            }
            instance = new ScreenshotTracker(trackingUrl);
            instance.start();
        }
        return instance;
    }

    /**
     * Start thread to use screenshots tracking service.
     */
    public static void stopTracking() {
        if (instance != null) {
            instance.stopThread();

            try {
                instance.join();
            } catch (InterruptedException e) {
                LOGGER.debug("Thread could not be joined.", e);
            }
            instance = null;
        }
    }

    /**
     * Send screenshot to tracking service.
     * 
     * @param path Path to screenshot.
     */
    public static void setCurrentScreenshot(final Path path) {
        currentFile = path;
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        if (!"OK".equals(client.startTracking())) {
            return;
        }
        String machinName;
        try {
            InetAddress localMachine = InetAddress.getLocalHost();
            machinName = localMachine.getHostName();
        } catch (UnknownHostException e1) {
            machinName = "[unknown]";
        }
        client.setTitle("Test started from " + machinName);
        // max 5 calls to cloud api in parallel.
        BlockingQueue<Runnable> queue = new SynchronousQueue<>();
        ExecutorService pool = new ThreadPoolExecutor(5, 5,
                0L, TimeUnit.MILLISECONDS,
                queue);
        while (!stopped) {
            // Set title if updated
            if (device != null && !title.equals(device.getName())) {
                title = device.getName();
                setDeviceName(title);
            }
            // Send screenshot given by mobile driver if not null
            Path toSend = currentFile;
            if (toSend != null) {
                currentFile = null;
                trackScreenshot(toSend, true);
            }
            // Send screenshot from api if no file from driver came within the last half second.
            if (System.currentTimeMillis() - lastSent > 1500 && device != null) {
                LOGGER.debug("Getting recent image from cloud api.");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        String imageOfDevice = CloudApiClient.getInstance().getImageOfDevice(device.getName(), lastSent);
                        if (imageOfDevice != null) {
                            trackScreenshot(Paths.get(imageOfDevice), false);
                        }
                    }
                };
                try {
                    lastSent = lastSent + 1500;
                    pool.execute(runnable);
                    Thread.sleep(200);
                } catch (RejectedExecutionException e) {
                    LOGGER.trace("Cloud API already called. Skip this time");
                } catch (InterruptedException e) {
                    LOGGER.trace("Sleep failed.");
                }
            }
            // Stop when mobiledriver did not send anything within the timeout.
            final long timeout = PropertyManager.getIntProperty(MobileProperties.MOBILE_SCREENSHOTTRACKER_TIMEOUT, 300000);
            if (System.currentTimeMillis() - lastSent > timeout) {
                stopTracking();
            }
        }
        pool.shutdown();
    }

    /**
     * Stop looking for new screenshots.
     */
    public void stopThread() {
        client.stopTracking();
        stopped = true;
    }

    /**
     * Send screenshot to tracking service.
     * 
     * @param path Path to screenshot.
     * @param fromClient indicates if screenshot comes from seetest client or cloud api.
     */
    public void trackScreenshot(final Path path, boolean fromClient) {
        final String source;
        if (fromClient) {
            lastSent = System.currentTimeMillis();
            source = "SeeTestClient";
        } else {
            source = "Cloud API";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.sendScreenshot(path, source);
            }
        }).start();
    }

    /**
     * Send current device name to Tracking service.
     * 
     * @param name name to set.
     */
    public void setDeviceName(String name) {
        client.setTitle(name);
    }
}
