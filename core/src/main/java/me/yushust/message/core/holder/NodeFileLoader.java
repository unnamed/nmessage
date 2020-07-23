package me.yushust.message.core.holder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface NodeFileLoader {

    NodeFile load(File file) throws IOException;

    NodeFile load(InputStream inputStream) throws IOException;

}
