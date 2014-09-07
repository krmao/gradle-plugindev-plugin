/*
 * Copyright 2014 Etienne Studer
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
 * limitations under the License.
 */

package nu.studer.gradle.plugindev

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * Task to generate the Gradle plugin descriptor file.
 */
class GeneratePluginDescriptorTask extends DefaultTask {

    @Input
    def pluginId

    @Input
    def pluginImplementationClass

    @OutputFile
    public File getPropertiesFile() {
        def resolvedPluginId = resolveAsString(pluginId, 'pluginId')
        return project.file("${project.buildDir}/plugindev/${resolvedPluginId}.properties")
    }

    @TaskAction
    def generate() {
        def resolvedPluginImplementationClass = resolveAsString(pluginImplementationClass, 'pluginImplementationClass')
        propertiesFile.text = "implementation-class=$resolvedPluginImplementationClass"
    }

    private String resolveAsString(def propertyValue, String propertyName) {
        if (propertyValue instanceof String) {
            propertyValue
        } else if (propertyValue instanceof Closure) {
            resolveAsString(propertyValue(), propertyName)
        } else {
            throw new IllegalArgumentException("Property '$propertyName' has value of unsupported type: ${propertyValue?.class}")
        }
    }

}