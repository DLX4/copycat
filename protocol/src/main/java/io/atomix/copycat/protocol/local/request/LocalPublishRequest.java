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
import io.atomix.copycat.protocol.request.PublishRequest;
import io.atomix.copycat.session.Event;

import java.util.List;
import java.util.Objects;

/**
 * Event publish request.
 * <p>
 * Publish requests are used by servers to publish event messages to clients. Event messages are
 * sequenced based on the point in the Raft log at which they were published to the client. The
 * {@link #eventIndex()} indicates the index at which the event was sent, and the {@link #previousIndex()}
 * indicates the index of the prior event messages sent to the client. Clients must ensure that event
 * messages are received in sequence by tracking the last index for which they received an event message
 * and validating {@link #previousIndex()} against that index.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class LocalPublishRequest extends LocalSessionRequest implements PublishRequest {
  private final long eventIndex;
  private final long previousIndex;
  private final List<Event<?>> events;

  protected LocalPublishRequest(long session, long eventIndex, long previousIndex, List<Event<?>> events) {
    super(session);
    this.eventIndex = eventIndex;
    this.previousIndex = previousIndex;
    this.events = events;
  }

  @Override
  public long eventIndex() {
    return eventIndex;
  }

  @Override
  public long previousIndex() {
    return previousIndex;
  }

  @Override
  public List<Event<?>> events() {
    return events;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClass(), session, eventIndex, previousIndex, events);
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof LocalPublishRequest) {
      LocalPublishRequest request = (LocalPublishRequest) object;
      return request.session == session
        && request.eventIndex == eventIndex
        && request.previousIndex == previousIndex
        && request.events.equals(events);
    }
    return false;
  }

  @Override
  public String toString() {
    return String.format("%s[session=%d, eventIndex=%d, previousIndex=%d, events=%s]", getClass().getSimpleName(), session, eventIndex, previousIndex, events);
  }

  /**
   * Publish request builder.
   */
  public static class Builder extends LocalSessionRequest.Builder<PublishRequest.Builder, PublishRequest> implements PublishRequest.Builder {
    private long eventIndex;
    private long previousIndex;
    private List<Event<?>> events;

    @Override
    public Builder withEventIndex(long index) {
      this.eventIndex = Assert.argNot(index, index < 1, "index cannot be less than 1");
      return this;
    }

    @Override
    public Builder withPreviousIndex(long index) {
      this.previousIndex = Assert.argNot(index, index < 0, "index cannot be less than 0");
      return this;
    }

    @Override
    public Builder withEvents(List<Event<?>> events) {
      this.events = events;
      return this;
    }

    @Override
    public PublishRequest build() {
      return new LocalPublishRequest(session, eventIndex, previousIndex, events);
    }
  }
}
