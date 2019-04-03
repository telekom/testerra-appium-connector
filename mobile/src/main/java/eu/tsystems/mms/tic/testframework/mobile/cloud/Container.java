package eu.tsystems.mms.tic.testframework.mobile.cloud;

import eu.tsystems.mms.tic.testframework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by rnhb on 09.12.2016.
 */
public class Container<T extends Nameable> implements Iterable<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Container.class);

    public Map<String, T> elements = new HashMap<>();

    public void add(T element) {
        if (!StringUtils.isEmpty(element.name())) {
            elements.put(element.name(), element);
        } else {
            LOGGER.warn("Tried to add element {} to {}. This is not allowed, its name has to be not empty.", element, this.getClass().getSimpleName());
        }
    }

    public T get(String name) {
        return elements.get(name);
    }

    public List<T> getByPartialName(String partialName) {
        List<T> list = new ArrayList<>();
        for (String name : elements.keySet()) {
            if (name.contains(partialName)) {
                list.add(elements.get(name));
            }
        }
        return list;
    }

    public boolean contains(Nameable nameable) {
        return elements.values().contains(nameable);
    }

    @Override
    public Iterator<T> iterator() {
        return elements.values().iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        elements.values().forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return elements.values().spliterator();
    }

    /**
     * For convenience.
     *
     * @return Number of contained elements.
     */
    public int size() {
        return elements.size();
    }
}
