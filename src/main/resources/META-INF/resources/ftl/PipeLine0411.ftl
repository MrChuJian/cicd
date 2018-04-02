<?xml version='1.0' encoding='UTF-8'?>
<flow-definition plugin="workflow-job@2.10">
  <actions/>
  <description></description>
  <keepDependencies>false</keepDependencies>
  <properties>
    <jenkins.model.BuildDiscarderProperty>
      <strategy class="hudson.tasks.LogRotator">
        <daysToKeep>4</daysToKeep>
        <numToKeep>-1</numToKeep>
        <artifactDaysToKeep>-1</artifactDaysToKeep>
        <artifactNumToKeep>-1</artifactNumToKeep>
      </strategy>
    </jenkins.model.BuildDiscarderProperty>
    <!-- <org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>
      <triggers>
        <hudson.triggers.TimerTrigger>
          <spec>H/15 * * * *</spec>
        </hudson.triggers.TimerTrigger>
      </triggers>
    </org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty> -->
  </properties>
  <definition class="org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition" plugin="workflow-cps@2.29">
    <script>
    stage(&apos;get_code&apos;){
        parallel &apos;get_code_app1_tools_1490252827659&apos;: {
            node(&apos;master&apos;) {
                parallel &apos;app1_tools_1490252827659&apos;: {
                    echo &apos;all tools are update.&apos;
                }
            }
        }, &apos;get_code_app1_git_1490252827659&apos;: {
            node(&apos;master&apos;) {
                parallel &apos;app1_git_1490252827659&apos;: {
                    echo &apos;https://github.com/jglick/simple-maven-project-with-tests.git&apos;
                    git &apos;https://github.com/jglick/simple-maven-project-with-tests.git&apos;
                }
            }
            
        }
    }
    stage(&apos;compile&apos;){
        node(&apos;master&apos;) {
           parallel &apos;app1_compile_1490252827659&apos;: {
        	   mvn &apos;-Dmaven.test.failure.ignore clean package&apos;
            }
        }
    }
    stage(&apos;UT&apos;){
        node(&apos;master&apos;) {
           parallel &apos;app1_unit_1490252827659&apos;: {
               junit &apos;**/target/surefire-reports/TEST-*.xml&apos;
            }
        }
    }
    stage(&apos;archive&apos;){
        node(&apos;master&apos;) {
           parallel &apos;app1_archive_1490252827659&apos;: {
               archive &apos;target/*.jar&apos;
            }
        }
    }
    </script>
    <sandbox>false</sandbox>
  </definition>
  <triggers/>
</flow-definition>