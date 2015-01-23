package org.kuali.common.jute.process;

import java.io.IOException;

public interface ProcessService {

    ProcessResult execute(String command) throws IOException;

    ProcessResult execute(ProcessContext context) throws IOException;

}
