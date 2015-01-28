package org.kuali.common.jute.json.jackson;

import static com.google.common.io.BaseEncoding.base64;

import com.google.common.io.BaseEncoding;

public final class Jackson {

    private Jackson() {}

    static final BaseEncoding DEFAULT_BASE_ENCODING = base64();

}
