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

import org.apache.hadoop.conf.Configuration;

/**
 * Local disk path allocator that is based on round robin.
 *
 * This class should eventually be removed once we push the 
 * LocalDiskPathAllocator class to Hadoop code base and make LocalDirAllocator
 * implement it.
 */
public class TezLocalDirAllocator extends LocalDirAllocator
  implements LocalDiskPathAllocator {

  public TezLocalDirAllocator(String hostname, String contextCfgItemName,
      Configuration conf) {

    super(contextCfgItemName);
  }
}
