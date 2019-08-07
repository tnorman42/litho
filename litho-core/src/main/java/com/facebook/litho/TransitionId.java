/*
 * Copyright 2018-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.litho;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents a unique id used for transitions, that is assigned to {@link LayoutOutput} in {@link
 * LayoutState} based on {@link Component}'s/{@link InternalNode}'s transitionKey and
 * transitionKeyType, later passed on to a {@link MountItem}, and used by {@link MountState} and
 * {@link TransitionManager}
 *
 * @see LayoutState#getTransitionIdForNode(InternalNode)
 */
public class TransitionId {
  @IntDef({Type.GLOBAL, Type.SCOPED, Type.AUTOGENERATED})
  @Retention(RetentionPolicy.SOURCE)
  @interface Type {
    int GLOBAL = 1;
    int SCOPED = 2;
    int AUTOGENERATED = 3;
  }

  final @Type int mType;

  /**
   * For {@link Type.GLOBAL} is a unique within the {@link ComponentTree} reference. For {@link
   * Type.SCOPED} - a unique within the owner {@link Component} reference. For {@link
   * Type.AUTOGENERATED} - a global key
   *
   * @see Component#getGlobalKey()
   */
  final String mReference;

  /**
   * The owner's global key IDs of {@link Type.SCOPED} type; {@code null} otherwise
   *
   * @see Component#getGlobalKey()
   */
  final String mExtraData;

  private final int mHashCode;

  TransitionId(@Type int type, String reference, String extraData) {
    if (reference == null) {
      throw new IllegalArgumentException("reference can't be null");
    }

    this.mType = type;
    this.mReference = reference;
    this.mExtraData = extraData;

    this.mHashCode =
        reference.hashCode() * 31 * 31
            + (extraData == null ? 0 : extraData.hashCode()) * 31
            + mType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TransitionId that = (TransitionId) o;
    return mType == that.mType
        && (mReference == that.mReference
            || (mReference != null && mReference.equals(that.mReference)))
        && (mExtraData == that.mExtraData
            || (mExtraData != null && mExtraData.equals(that.mExtraData)));
  }

  @Override
  public int hashCode() {
    return mHashCode;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("TransitionId{");
    sb.append("\"").append(mReference).append("\", ");
    switch (mType) {
      case Type.GLOBAL:
        sb.append("GLOBAL");
        break;

      case Type.SCOPED:
        sb.append("SCOPED(").append(mExtraData).append(")");
        break;

      case Type.AUTOGENERATED:
        sb.append("AUTOGENERATED");
        break;
    }
    sb.append("}");
    return sb.toString();
  }
}
