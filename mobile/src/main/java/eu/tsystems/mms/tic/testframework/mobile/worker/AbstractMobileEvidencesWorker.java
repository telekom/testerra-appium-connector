package eu.tsystems.mms.tic.testframework.mobile.worker;

import eu.tsystems.mms.tic.testframework.execution.worker.finish.AbstractEvidencesWorker;

public abstract class AbstractMobileEvidencesWorker extends AbstractEvidencesWorker implements MobileVideoHandlerInterface {

    protected abstract void collect();

}
