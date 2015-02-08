package org.kuali.common.jute.base;

public final class Reducers {

    private Reducers() {}

    public static Reducer<Integer, Integer> sumIntegers() {
        return IntegerReducers.SUM;
    }

    public static Reducer<Long, Long> sumLongs() {
        return LongReducers.SUM;
    }

    public static Reducer<Double, Double> sumDoubles() {
        return DoubleReducers.SUM;
    }

    private enum IntegerReducers implements Reducer<Integer, Integer> {
        SUM {
            @Override
            public Integer apply(Integer b, Integer a) {
                return b + a;
            }
        },
    }

    private enum LongReducers implements Reducer<Long, Long> {
        SUM {
            @Override
            public Long apply(Long b, Long a) {
                return b + a;
            }
        },
    }

    private enum DoubleReducers implements Reducer<Double, Double> {
        SUM {
            @Override
            public Double apply(Double b, Double a) {
                return b + a;
            }
        },
    }

}
