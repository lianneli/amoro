/*
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

package org.apache.amoro.spark.sql.catalyst.analysis

import scala.collection.JavaConverters.seqAsJavaList

import org.apache.amoro.spark.command.MigrateToArcticCommand
import org.apache.amoro.spark.sql.catalyst.plans
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.rules.Rule

case class ResolveArcticCommand(spark: SparkSession) extends Rule[LogicalPlan] {

  override def apply(plan: LogicalPlan): LogicalPlan = plan resolveOperators {
    case plans.MigrateToArcticStatement(source, target) =>
      val command = MigrateToArcticCommand.newBuilder(spark)
        .withSource(seqAsJavaList(source))
        .withTarget(seqAsJavaList(target))
        .build()
      plans.MigrateToArcticLogicalPlan(command)
  }
}
