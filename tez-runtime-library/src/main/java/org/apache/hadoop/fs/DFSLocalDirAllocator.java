/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.fs;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

/**
 * Local disk path allocator for each node on DFS.
 * In order to identify each node on the DFS namespace, its hostname will be
 * used as part of the directory path. The assumption is that the DFS will be
 * able to create this directory on the local disk of the node specified by
 * the hostname.
 */
public class DFSLocalDirAllocator implements LocalDiskPathAllocator {
  private static final String NODE_LOCAL_DIR_PREFIX = YarnConfiguration.YARN_PREFIX
    + "local.dir.prefix";

  private static final String NODE_LOCAL_DIR_SUFFIX = YarnConfiguration.YARN_PREFIX
    + "local.dir.suffix";

  /**
   * Absolute path of the local directory designated for each node.
   */
  private final Path nodeLocalDir;

  public DFSLocalDirAllocator(String hostname, String contextCfgItemName,
      Configuration conf) {

    String nodeLocalDirPrefix = conf.get(NODE_LOCAL_DIR_PREFIX);
    String nodeLocalDirSuffix = conf.get(NODE_LOCAL_DIR_SUFFIX);
    StringBuilder sb = new StringBuilder();
    sb.append(nodeLocalDirPrefix).append(Path.SEPARATOR)
      .append(hostname).append(Path.SEPARATOR).append(nodeLocalDirSuffix);

    this.nodeLocalDir = new Path(sb.toString());
  }

  private Path getPath(String pathStr) {
    return new Path(nodeLocalDir, pathStr);
  }

  public Path getLocalPathForWrite(String pathStr, 
      Configuration conf) throws IOException {
    
    return getPath(pathStr);
  }
  
  public Path getLocalPathForWrite(String pathStr, long size, 
      Configuration conf) throws IOException {

    return getPath(pathStr);
  }

  public Path getLocalPathForWrite(String pathStr, long size, 
      Configuration conf,
      boolean checkWrite) throws IOException {

    return getPath(pathStr);
  }
  
  public Path getLocalPathToRead(String pathStr, 
      Configuration conf) throws IOException {

    return getPath(pathStr);
  }
  
  public Iterable<Path> getAllLocalPathsToRead(String pathStr, 
      Configuration conf) throws IOException {

    return Arrays.asList(getPath(pathStr));
  }

  public boolean ifExists(String pathStr, Configuration conf) 
    throws IOException {

    FileSystem fs = FileSystem.get(conf);
    return fs.exists(getPath(pathStr));
  }

  public String[] getLocalDirs(Configuration conf) {
    return new String[] {nodeLocalDir.toString()};
  }
}
