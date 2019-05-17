/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.commons.service.dao;

import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Merger<I,O> {

    private class FlaggedEntry {

        private final O item;

        private boolean created;

        private boolean updated;

        private boolean deleted;

        private FlaggedEntry(final O convertedItem) {
            this.item = convertedItem;
        }

    }

    /**
     * Merge a collection of items
     *
     * @param inputs items to merge
     * @return
     * @throws ServiceException
     */
    public boolean merge(final Collection<I> inputs) throws ServiceException {

        Map<Object, FlaggedEntry> incomingRecords = createIncomming(inputs);

        Map<Object, FlaggedEntry> currentRecords = createCurrent(inputs, incomingRecords);

        merge(incomingRecords, currentRecords);

        return save(currentRecords);
    }

    private boolean save(final Map<Object, FlaggedEntry> current) throws ServiceException {

        boolean updated=false;

        for (Merger<I, O>.FlaggedEntry flaggedItem : current.values()) {

            if (flaggedItem.created) {

                insert(flaggedItem.item);

                updated|=true;

            } else if (flaggedItem.updated) {

                update(flaggedItem.item);

                updated|=true;

            } else if (flaggedItem.deleted) {

                delete(flaggedItem.item);

                updated|=true;

            }

        }

        return updated;

    }

    private void merge(final Map<Object, FlaggedEntry> incoming, final Map<Object, FlaggedEntry> current) throws ServiceException {

        for (Merger<I, O>.FlaggedEntry flaggedItem : incoming.values()) {

            merge(flaggedItem, current);

        }

    }

    private void merge(final Merger<I, O>.FlaggedEntry incommingItem, final Map<Object, FlaggedEntry> current) throws ServiceException {
        Object key=getUniqKey(incommingItem.item);
        Merger<I, O>.FlaggedEntry currentItem = current.get(key);
        if (currentItem == null) {
            incommingItem.created=true;
            current.put(key, incommingItem);
        } else {
            currentItem.updated=merge(incommingItem.item, currentItem.item);
        }
    }

    private Map<Object, FlaggedEntry> createCurrent(final Collection<I> input,
                                                    final Map<Object, FlaggedEntry> incomming) throws ServiceException {
        Map<Object, FlaggedEntry> current=new HashMap<>();

        for (O item: loadCurrents(input)) {
            Merger<I, O>.FlaggedEntry flaggedItem = new FlaggedEntry(item);
            Object key=getUniqKey(item);
            flaggedItem.deleted = !incomming.containsKey(key);
            current.put(key, flaggedItem);
        }

        return current;
    }

    private Map<Object, FlaggedEntry> createIncomming(final Collection<I> inputs) throws ServiceException {
        Map<Object, FlaggedEntry> incomming = new HashMap<>();
        for (I input : inputs) {
            for (O convertedItem : convert(input)) {
                incomming.put(getUniqKey(convertedItem), new FlaggedEntry(convertedItem));
            }
        }
        return incomming;
    }

    /**
     * Get unique key of an item
     *
     * @param item item to check
     * @return unique key - must implement equal/hash correctly
     * @throws ServiceException exception during processing
     */
    protected abstract Object getUniqKey(final O item) throws ServiceException;

    /**
     * Convert an item
     *
     * @param input
     * @return resulting converted items
     * @throws ServiceException exception during processing
     */
    protected abstract Collection<O> convert(final I input) throws ServiceException;

    /**
     * retrieve current items
     *
     * @param input input items
     * @return items to merge
     * @throws ServiceException exception during processing
     */
    protected abstract Collection<O> loadCurrents(final Collection<I> input) throws ServiceException;

    /**
     * Merge 2 items
     *
     * @param incoming new item - not saved
     * @param existing existing item - will be saved
     * @return true if an update was done
     * @throws ServiceException exception during processing
     */
    protected abstract boolean merge(final O incoming, final O existing) throws ServiceException;


    /**
     * Insert a new record
     *
     * @param item item to insert
     * @throws ServiceException exception during processing
     */
    protected abstract void insert(final O item) throws ServiceException;

    /**
     * Update an existing record
     *
     * @param item
     * @throws ServiceException exception during processing
     */
    protected abstract void update(final O item) throws ServiceException;

    /**
     * delete an existing record
     *
     * @param item
     * @throws ServiceException exception during processing
     */
    protected abstract void delete(final O item) throws ServiceException;
}