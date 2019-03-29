/*
   Copyright 2014-2018 Sam Gleske - https://github.com/samrocketman/jervis

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   */
/*
   A pipeline interface to check if the current build is being done from a Git
   tag.  Designed for multibranch pipeline jobs.
 */

import hudson.model.Job
import jenkins.plugins.git.GitTagSCMHead
import org.jenkinsci.plugins.workflow.multibranch.BranchJobProperty


@NonCPS
Boolean isTagBuild(Job build_parent) {
    build_parent?.getProperty(BranchJobProperty)?.branch?.head instanceof GitTagSCMHead
}

Boolean call() {
    isTagBuild(currentBuild.rawBuild.parent)
}
