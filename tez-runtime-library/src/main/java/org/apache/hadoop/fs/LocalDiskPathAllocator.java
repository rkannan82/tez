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

/**
 * Interface to get file/directory paths from the local disk namespace.
 * Modules that need to get a handle to the implementation should do so using
 * {@link org.apache.hadoop.fs.LocalDiskUtil}.
 *
 * This abstraction is provided for 2 reasons:
 * <pre>
 * 1. There can be more than one algorithm to determine how to allocate local
 * disks.
 *
 * 2. A distributed file system that supports full POFSIX read/write can choose
 * to expose local disks using the HDFS interface itself and thereby have a way
 * to create node local paths inside the DFS. For e.g., the local dir for node1
 * and node2 on DFS can be:
 *
 * /path/to/local/dir/node1_fqdn/
 * /path/to/local/dir/node2_fqdn/
 *
 * where these directories actually reside on node1 and node2 respectively.
 * </pre>
 */
public interface LocalDiskPathAllocator {
  /** Get a path from the local disk. This method should be used if the size of 
   *  the file is not known apriori.
   *  @param pathStr the requested path
   *  @param conf the Configuration object
   *  @return the complete path to the file on a local disk
   *  @throws IOException
   */
  Path getLocalPathForWrite(String pathStr, 
      Configuration conf) throws IOException;
  
  /** Get a path from the local disk. Pass size as 
   *  SIZE_UNKNOWN if not known apriori.
   *  @param pathStr the requested path
   *  @param size the size of the file that is going to be written
   *  @param conf the Configuration object
   *  @return the complete path to the file on a local disk
   *  @throws IOException
   */
  Path getLocalPathForWrite(String pathStr, long size, 
      Configuration conf) throws IOException;
  
  /** Get a path from the local disk. Pass size as 
   *  SIZE_UNKNOWN if not known apriori.
   *  @param pathStr the requested path
   *  @param size the size of the file that is going to be written
   *  @param conf the Configuration object
   *  @param checkWrite ensure that the path is writable
   *  @return the complete path to the file on a local disk
   *  @throws IOException
   */
  Path getLocalPathForWrite(String pathStr, long size, 
                                   Configuration conf,
                                   boolean checkWrite) throws IOException;
  
  /** Get a path from the local disk for reading.
   *  @param pathStr the requested file (this will be searched)
   *  @param conf the Configuration object
   *  @return the complete path to the file on a local disk
   *  @throws IOException
   */
  Path getLocalPathToRead(String pathStr, 
      Configuration conf) throws IOException;
  
  /**
   * Get all of the paths that currently exist in the working directories.
   * @param pathStr the path underneath the roots
   * @param conf the configuration to look up the roots in
   * @return all of the paths that exist under any of the roots
   * @throws IOException
   */
  Iterable<Path> getAllLocalPathsToRead(String pathStr, 
                                               Configuration conf
                                               ) throws IOException;

  /** We search through all the configured dirs for the file's existence
   *  and return true when we find  
   *  @param pathStr the requested file (this will be searched)
   *  @param conf the Configuration object
   *  @return true if files exist. false otherwise
   *  @throws IOException
   */
  boolean ifExists(String pathStr, Configuration conf)
    throws IOException;

  /**
   * Returns the local directories used to construct the paths.
   */
  String[] getLocalDirs(Configuration conf);
}
