package com.watent.framework.context;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamSource {

    InputStream getInputStream() throws IOException;
}
