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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

/**
 * Helper class to get a handle to LocalDiskPathAllocator and the FileSystem
 * that is used to manage those paths.
 */
public final class LocalDiskUtil {
  /**
   * Determines if a Distributed File System should be used to manage the
   * allocated local disk paths. This defaults to false.
   */
  private static final String USE_DFS_LOCAL_DISK_PATH_ALLOCATOR = YarnConfiguration.YARN_PREFIX
    + "local.disk.path.allocator.use-dfs";

  /**
   * Property that determines the custom class for local disk path allocation.
   */
  private static final String LOCAL_DISK_PATH_ALLOCATOR_CLASS = YarnConfiguration.YARN_PREFIX
    + "local.disk.path.allocator.class";

  /**
   * Returns the local disk path allocator for given host.
   */
  public static LocalDiskPathAllocator getPathAllocator(String hostname,
      Configuration conf, String ctxName) {

    String customClass = conf.get(LOCAL_DISK_PATH_ALLOCATOR_CLASS);
    if (customClass == null) {
      if (isManagedByDFS(conf)) {
        return new DFSLocalDirAllocator(hostname, ctxName, conf);
      } else {
        return new TezLocalDirAllocator(hostname, ctxName, conf);
      }
    } else {
      Class<? extends LocalDiskPathAllocator> clazz = conf.getClass(
          LOCAL_DISK_PATH_ALLOCATOR_CLASS,
          null,
          LocalDiskPathAllocator.class);

      try {
        return clazz.getDeclaredConstructor(String.class, String.class,
            Configuration.class)
          .newInstance(hostname, ctxName, conf);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * Returns the local disk path allocator for current host.
   */
  public static LocalDiskPathAllocator getPathAllocator(Configuration conf,
      String ctxName) {

    String hostname = System.getenv(
        ApplicationConstants.Environment.NM_HOST.toString());

    return getPathAllocator(hostname, conf, ctxName);
  }

  /**
   * Returns the file system that is used to manage the allocated local disk
   * paths.
   */
  public static FileSystem getFileSystem(Configuration conf) throws IOException {
    return isManagedByDFS(conf) ? FileSystem.get(conf) : FileSystem.getLocal(conf);
  }

  /**
   * Determines if the local disk path allocation is managed by DFS.
   */
  public static boolean isManagedByDFS(Configuration conf) {
    return conf.getBoolean(USE_DFS_LOCAL_DISK_PATH_ALLOCATOR, false);
  }
}
