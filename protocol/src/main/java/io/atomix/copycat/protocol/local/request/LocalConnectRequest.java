/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package io.atomix.copycat.protocol.local.request;

import io.atomix.catalyst.util.Assert;
import io.atomix.copycat.protocol.request.ConnectRequest;

import java.util.Objects;

/**
 * Connect client request.
 * <p>
 * Connect requests are sent by clients to specific servers when first establishing a connection.
 * Connections must be associated with a specific {@link #client() client ID} and must be established
 * each time the client switches servers. A client may only be connected to a single server at any
 * given time.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class LocalConnectRequest extends AbstractLocalRequest implements ConnectRequest {
  private final String client;

  protected LocalConnectRequest(String client) {
    this.client = client;
  }

  @Override
  public String client() {
    return client;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClass(), client);
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof LocalConnectRequest && ((LocalConnectRequest) object).client.equals(client);
  }

  @Override
  public String toString() {
    return String.format("%s[client=%s]", getClass().getSimpleName(), client);
  }

  /**
   * Register client request builder.
   */
  public static class Builder extends AbstractLocalRequest.Builder<ConnectRequest.Builder, ConnectRequest> implements ConnectRequest.Builder {
    private String client;

    @Override
    public Builder withClient(String client) {
      this.client = Assert.notNull(client, "client");
      return this;
    }

    @Override
    public ConnectRequest build() {
      return new LocalConnectRequest(client);
    }
  }
}
