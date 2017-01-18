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
package io.atomix.copycat.server.protocol.local.request;

import io.atomix.catalyst.util.Assert;
import io.atomix.copycat.protocol.local.request.AbstractLocalRequest;
import io.atomix.copycat.server.cluster.Member;
import io.atomix.copycat.server.protocol.request.ConfigurationRequest;

import java.util.Objects;

/**
 * Configuration change request.
 * <p>
 * Configuration change requests are the basis for members joining and leaving the cluster.
 * When a member wants to join or leave the cluster, it must submit a configuration change
 * request to the leader where the change will be logged and replicated.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public abstract class LocalConfigurationRequest extends AbstractLocalRequest implements ConfigurationRequest {
  protected final Member member;

  protected LocalConfigurationRequest(Member member) {
    this.member = member;
  }

  @Override
  public Member member() {
    return member;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClass(), member);
  }

  @Override
  public boolean equals(Object object) {
    if (getClass().isAssignableFrom(object.getClass())) {
      return ((LocalConfigurationRequest) object).member.equals(member);
    }
    return false;
  }

  @Override
  public String toString() {
    return String.format("%s[member=%s]", getClass().getSimpleName(), member);
  }

  /**
   * Configuration request builder.
   */
  public static abstract class Builder<T extends ConfigurationRequest.Builder<T, U>, U extends ConfigurationRequest> extends AbstractLocalRequest.Builder<T, U> {
    protected Member member;

    /**
     * Sets the request member.
     *
     * @param member The request member.
     * @return The request builder.
     * @throws NullPointerException if {@code member} is null
     */
    @SuppressWarnings("unchecked")
    public T withMember(Member member) {
      this.member = Assert.notNull(member, "member");
      return (T) this;
    }
  }
}
