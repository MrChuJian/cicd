<?xml version='1.0' encoding='UTF-8'?>
<flow-definition plugin="workflow-job@2.10">
  <actions/>
  <description></description>
  <displayName>${displayName}</displayName>
  <keepDependencies>false</keepDependencies>
  <properties>
    <jenkins.model.BuildDiscarderProperty>
      <strategy class="hudson.tasks.LogRotator">
        <daysToKeep>${daysToKeep}</daysToKeep>
        <numToKeep>${numToKeep}</numToKeep>
        <artifactDaysToKeep>${daysToKeep}</artifactDaysToKeep>
        <artifactNumToKeep>${numToKeep}</artifactNumToKeep>
      </strategy>
    </jenkins.model.BuildDiscarderProperty>
    <#if hasTrigger >
    <org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>
      <triggers>
      	<#if hasTimeTrigger >
        <hudson.triggers.TimerTrigger>
          <spec>${spec}</spec>
        </hudson.triggers.TimerTrigger>
        </#if>
        <#if hasSCMTrigger >
        <hudson.triggers.SCMTrigger>
          <spec>${spec}</spec>
          <ignorePostCommitHooks>${ignoreHook}</ignorePostCommitHooks>
        </hudson.triggers.SCMTrigger>
        </#if>
      </triggers>
    </org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>
    </#if>
  </properties>
  <definition class="org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition" plugin="workflow-cps@2.29">
    <script>${script}</script>
    <sandbox>false</sandbox>
  </definition>
  <triggers/>
  <quietPeriod>${quietPeriod}</quietPeriod>
</flow-definition>