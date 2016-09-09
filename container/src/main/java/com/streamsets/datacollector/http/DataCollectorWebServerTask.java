/**
 * Copyright 2016 StreamSets Inc.
 *
 * Licensed under the Apache Software Foundation (ASF) under one
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
package com.streamsets.datacollector.http;

import com.google.common.collect.ImmutableBiMap;
import com.streamsets.datacollector.main.BuildInfo;
import com.streamsets.datacollector.main.RuntimeInfo;
import com.streamsets.datacollector.util.Configuration;
import com.streamsets.lib.security.http.RemoteSSOService;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

public class DataCollectorWebServerTask extends WebServerTask {
  private BuildInfo buildInfo;

  @Inject
  public DataCollectorWebServerTask(
      BuildInfo buildInfo,
      RuntimeInfo runtimeInfo,
      Configuration conf,
      Set<ContextConfigurator> contextConfigurators,
      Set<WebAppProvider> webAppProviders
  ) {
    super(buildInfo, runtimeInfo, conf, contextConfigurators, webAppProviders);
    this.buildInfo = buildInfo;
  }

  @Override
  protected String getAppAuthToken(Configuration appConfiguration) {
    return getRuntimeInfo().getAppAuthToken();
  }

  @Override
  protected String getComponentId(Configuration appConfiguration) {
    return getRuntimeInfo().getId();
  }

  @Override
  protected Map<String, String> getRegistrationAttributes() {
    return ImmutableBiMap.<String, String>builder()
        .putAll(super.getRegistrationAttributes())
        .put("sdcJavaVersion", System.getProperty("java.runtime.version"))
        .put("sdcVersion", buildInfo.getVersion())
        .put("sdcBuildDate", buildInfo.getBuiltDate())
        .put("sdcRepoSha", buildInfo.getBuiltRepoSha())
        .build();
  }

  protected boolean isDisconnectedSSOModeEnabled() {
    return true;
  }

}
