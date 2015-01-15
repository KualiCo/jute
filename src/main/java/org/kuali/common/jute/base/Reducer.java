package org.kuali.common.jute.base;

public interface Reducer<A, B> {

	B apply(B b, A a);

}
