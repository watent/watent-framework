package com.watent.framework.context;

import java.io.IOException;

public interface ResourceLoader {

    Resource getResource(String location) throws IOException;

}
