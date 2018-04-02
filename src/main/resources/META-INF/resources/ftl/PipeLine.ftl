<?xml version='1.0' encoding='UTF-8'?>
<flow-definition plugin="workflow-job@2.10">
  <actions/>
  <description></description>
  <displayName>${displayName}</displayName>
  <keepDependencies>false</keepDependencies>
  <properties>
    <jenkins.model.BuildDiscarderProperty>
      <strategy class="hudson.tasks.LogRotator">
        <daysToKeep>-1</daysToKeep>
        <numToKeep>30</numToKeep>
        <artifactDaysToKeep>-1</artifactDaysToKeep>
        <artifactNumToKeep>30</artifactNumToKeep>
      </strategy>
    </jenkins.model.BuildDiscarderProperty>
    <#if hasTrigger >
    <org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>
      <triggers>
        <hudson.triggers.TimerTrigger>
          <spec>${spec}</spec>
        </hudson.triggers.TimerTrigger>
      </triggers>
    </org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>
    </#if>
  </properties>
  <definition class="org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition" plugin="workflow-cps@2.29">
    <script>${script}</script>
    <sandbox>false</sandbox>
  </definition>
  <triggers/>
  <quietPeriod>5</quietPeriod>
</flow-definition>