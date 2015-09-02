package com.garpr.android.misc;


import android.widget.Filter;

import com.garpr.android.models.BaseDateWrapper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public final class ListUtils {


    public static ArrayList<? extends AlphabeticallyComparable> createAlphabeticalList(
            final ArrayList<? extends AlphabeticallyComparable> list,
            final AlphabeticalSectionCreator creator) {
        final ArrayList<AlphabeticallyComparable> newList = new ArrayList<>(list.size());
        sortOutLetters(list, newList, creator);
        sortOutDigits(list, newList, creator);
        sortOutOthers(list, newList, creator);

        newList.trimToSize();
        return newList;
    }


    public static Filter createBasicFilter(final ArrayList<? extends BasicFilterable> list,
            final FilterListener listener) {
        return new BasicFilter(list, listener);
    }


    public static ArrayList<? extends MonthlyComparable> createMonthlyList(
            final ArrayList<? extends MonthlyComparable> list,
            final MonthlySectionCreator creator) {
        final ArrayList<MonthlyComparable> newList = new ArrayList<>(list.size());
        String lastMonth = null, lastYear = null;

        for (final MonthlyComparable mc : list) {
            final BaseDateWrapper dw = mc.getDateWrapper();
            final String month = dw.getMonth();
            final String year = dw.getYear();

            if (!month.equalsIgnoreCase(lastMonth) || !year.equalsIgnoreCase(lastYear)) {
                lastMonth = month;
                lastYear = year;

                newList.add(creator.createMonthlySection(dw));
            }

            newList.add(mc);
        }

        newList.trimToSize();
        return newList;
    }


    public static Filter createSpecialFilter(final ArrayList<? extends SpecialFilterable> list,
            final FilterListener listener) {
        return new SpecialFilter(list, listener);
    }


    private static void sortOutDigits(final ArrayList<? extends AlphabeticallyComparable> oldList,
            final ArrayList<AlphabeticallyComparable> newList, final AlphabeticalSectionCreator creator) {
        final int size = newList.size();

        for (final AlphabeticallyComparable item : oldList) {
            final char character = item.getFirstCharOfName();

            if (Character.isDigit(character)) {
                newList.add(item);
            }
        }

        if (size < newList.size()) {
            newList.add(size, creator.createDigitSection());
        }
    }


    private static void sortOutLetters(final ArrayList<? extends AlphabeticallyComparable> oldList,
            final ArrayList<AlphabeticallyComparable> newList, final AlphabeticalSectionCreator creator) {
        char lastLetter = ' ';
        boolean lastLetterIsSet = false;

        for (final AlphabeticallyComparable item : oldList) {
            final char character = item.getFirstCharOfName();

            if (Character.isLetter(character)) {
                final char letter = Character.toUpperCase(character);

                if (!lastLetterIsSet || lastLetter != letter) {
                    lastLetterIsSet = true;
                    lastLetter = letter;
                    newList.add(creator.createLetterSection(String.valueOf(letter)));
                }

                newList.add(item);
            }
        }
    }


    private static void sortOutOthers(final ArrayList<? extends AlphabeticallyComparable> oldList,
            final ArrayList<AlphabeticallyComparable> newList, final AlphabeticalSectionCreator creator) {
        final int size = newList.size();

        for (final AlphabeticallyComparable item : oldList) {
            final char character = item.getFirstCharOfName();

            if (!Character.isLetterOrDigit(character)) {
                newList.add(item);
            }
        }

        if (size < newList.size()) {
            newList.add(size, creator.createOtherSection());
        }
    }




    private static abstract class BaseFilter<T> extends Filter {


        private final ArrayList<T> mList;
        private final ListUtils.FilterListener mListener;


        private BaseFilter(final ArrayList<T> list, final ListUtils.FilterListener listener) {
            mList = list;
            mListener = listener;
        }


        @Override
        protected final FilterResults performFiltering(final CharSequence constraint) {
            final String query = constraint.toString().trim().toLowerCase();
            final ArrayList<T> list = performFiltering(mList, query);

            final FilterResults filterResults = new FilterResults();
            filterResults.count = list.size();
            filterResults.values = list;

            return filterResults;
        }


        protected abstract ArrayList<T> performFiltering(final ArrayList<T> oldList, final String query);


        @Override
        @SuppressWarnings("unchecked")
        protected final void publishResults(final CharSequence constraint, final FilterResults results) {
            if (mListener.isAlive()) {
                final ArrayList<T> list = (ArrayList<T>) results.values;
                mListener.onFilterComplete(list);
            }
        }


    }


    public static class BasicFilter extends BaseFilter<BasicFilterable> {


        @SuppressWarnings("unchecked")
        private BasicFilter(final ArrayList<? extends BasicFilterable> list, final ListUtils.FilterListener listener) {
            super((ArrayList<BasicFilterable>) list, listener);
        }


        @Override
        protected ArrayList<BasicFilterable> performFiltering(final ArrayList<BasicFilterable> oldList,
                final String query) {
            final ArrayList<BasicFilterable> newList = new ArrayList<>(oldList.size());

            for (final BasicFilterable item : oldList) {
                final String name = item.getName().toLowerCase();

                if (name.contains(query)) {
                    newList.add(item);
                }
            }

            return newList;
        }


    }


    public static abstract class FilterListener<T extends BasicFilterable> {


        private final WeakReference<Heartbeat> mHeartbeat;


        public FilterListener(final Heartbeat heartbeat) {
            mHeartbeat = new WeakReference<>(heartbeat);
        }


        private boolean isAlive() {
            final Heartbeat heartbeat = mHeartbeat.get();
            return heartbeat != null && heartbeat.isAlive();
        }


        public abstract void onFilterComplete(final ArrayList<T> list);


    }


    public static class SpecialFilter extends BaseFilter<SpecialFilterable> {


        @SuppressWarnings("unchecked")
        private SpecialFilter(final ArrayList<? extends SpecialFilterable> list, final ListUtils.FilterListener listener) {
            super((ArrayList<SpecialFilterable>) list, listener);
        }


        @Override
        protected ArrayList<SpecialFilterable> performFiltering(final ArrayList<SpecialFilterable>
                oldList, final String query) {
            final ArrayList<SpecialFilterable> newList = new ArrayList<>(oldList.size());

            for (int i = 0; i < oldList.size(); ++i) {
                final SpecialFilterable item = oldList.get(i);

                if (item.isBasicItem()) {
                    final String name = item.getName().toLowerCase();

                    if (name.contains(query)) {
                        SpecialFilterable title = null;

                        for (int j = i - 1; title == null; --j) {
                            final SpecialFilterable item2 = oldList.get(j);

                            if (item2.isSpecialItem()) {
                                title = item2;
                            }
                        }

                        if (!newList.contains(title)) {
                            newList.add(title);
                        }

                        newList.add(item);
                    }
                }
            }

            return newList;
        }


    }


    public interface AlphabeticallyComparable {


        char getFirstCharOfName();


    }


    public interface AlphabeticalSectionCreator {


        AlphabeticallyComparable createDigitSection();


        AlphabeticallyComparable createLetterSection(final String letter);


        AlphabeticallyComparable createOtherSection();


    }


    public interface BasicFilterable {


        String getName();


    }


    public interface MonthlyComparable {


        BaseDateWrapper getDateWrapper();


    }


    public interface MonthlySectionCreator {


        MonthlyComparable createMonthlySection(final BaseDateWrapper dateWrapper);


    }


    public interface SpecialFilterable extends BasicFilterable {


        boolean isBasicItem();


        boolean isSpecialItem();


    }


}
