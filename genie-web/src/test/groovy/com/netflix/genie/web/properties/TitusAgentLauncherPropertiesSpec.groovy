/*
 *
 *  Copyright 2020 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.genie.web.properties

import org.springframework.util.unit.DataSize
import spock.lang.Specification

import java.time.Duration

class TitusAgentLauncherPropertiesSpec extends Specification {

    def "Defaults, getter, setter"() {

        TitusAgentLauncherProperties p = new TitusAgentLauncherProperties()

        expect:
        !p.isEnabled()
        p.getEndpoint() == URI.create("https://example-titus-endpoint.tld:1234")
        p.getEntryPointTemplate() == [
            "/bin/genie-agent",
            "exec",
            "--api-job",
            "--launchInJobDirectory",
            "--job-id", TitusAgentLauncherProperties.JOB_ID_PLACEHOLDER,
            "--server-host", TitusAgentLauncherProperties.SERVER_HOST_PLACEHOLDER,
            "--server-port", TitusAgentLauncherProperties.SERVER_PORT_PLACEHOLDER
        ]
        p.getOwnerEmail() == "owners@genie.tld"
        p.getApplicationName() == "genie"
        p.getCapacityGroup() == "default"
        p.getSecurityAttributes() == [:]
        p.getSecurityGroups() == []
        p.getIAmRole() == "arn:aws:iam::000000000:role/SomeProfile"
        p.getImageName() == "image-name"
        p.getImageTag() == "latest"
        p.getDiskSize() == DataSize.ofGigabytes(10)
        p.getNetworkBandwidth() == DataSize.ofMegabytes(7)
        p.getRetries() == 0
        p.getRuntimeLimit() == Duration.ofHours(12)
        p.getGenieServerHost() == "example.genie.tld"
        p.getGenieServerPort() == 9090
        p.getHealthIndicatorMaxSize() == 100
        p.getHealthIndicatorExpiration() == Duration.ofMinutes(30)
        p.getAdditionalEnvironment() == [:]

        when:
        p.setEnabled(true)
        p.setEndpoint(URI.create("https://test-titus-endpoint.tld:4321"))
        p.setEntryPointTemplate([
            "/usr/local/bin/genie-agent",
            "exec",
            "--server-host", TitusAgentLauncherProperties.SERVER_HOST_PLACEHOLDER,
            "--server-port", TitusAgentLauncherProperties.SERVER_PORT_PLACEHOLDER,
            "--job-id", TitusAgentLauncherProperties.JOB_ID_PLACEHOLDER,
            "--api-job",
            "--launchInJobDirectory",
        ])
        p.setOwnerEmail("genie@genie.tld")
        p.setApplicationName("genie-foo")
        p.setCapacityGroup("genie-cg")
        p.setSecurityAttributes([foo: "bar"])
        p.setSecurityGroups(["g1", "g2"])
        p.setIAmRole("arn:aws:iam::99999999:role/SomeOtherProfile")
        p.setImageName("another-image-name")
        p.setImageTag("latest.release")
        p.setDiskSize(DataSize.ofGigabytes(20))
        p.setNetworkBandwidth(DataSize.ofMegabytes(14))
        p.setRetries(1)
        p.setRuntimeLimit(Duration.ofHours(24))
        p.setGenieServerHost("genie.tld")
        p.setGenieServerPort(9191)
        p.setHealthIndicatorMaxSize(200)
        p.setHealthIndicatorExpiration(Duration.ofMinutes(15))
        p.setAdditionalEnvironment([FOO: "BAR"])

        then:
        p.isEnabled()
        p.getEndpoint() == URI.create("https://test-titus-endpoint.tld:4321")
        p.getEntryPointTemplate() == [
            "/usr/local/bin/genie-agent",
            "exec",
            "--server-host", TitusAgentLauncherProperties.SERVER_HOST_PLACEHOLDER,
            "--server-port", TitusAgentLauncherProperties.SERVER_PORT_PLACEHOLDER,
            "--job-id", TitusAgentLauncherProperties.JOB_ID_PLACEHOLDER,
            "--api-job",
            "--launchInJobDirectory",
        ]
        p.getOwnerEmail() == "genie@genie.tld"
        p.getApplicationName() == "genie-foo"
        p.getCapacityGroup() == "genie-cg"
        p.getSecurityAttributes() == [foo: "bar"]
        p.getSecurityGroups() == ["g1", "g2"]
        p.getIAmRole() == "arn:aws:iam::99999999:role/SomeOtherProfile"
        p.getImageName() == "another-image-name"
        p.getImageTag() == "latest.release"
        p.getDiskSize() == DataSize.ofGigabytes(20)
        p.getNetworkBandwidth() == DataSize.ofMegabytes(14)
        p.getRetries() == 1
        p.getRuntimeLimit() == Duration.ofHours(24)
        p.getGenieServerHost() == "genie.tld"
        p.getGenieServerPort() == 9191
        p.getHealthIndicatorMaxSize() == 200
        p.getHealthIndicatorExpiration() == Duration.ofMinutes(15)
        p.getAdditionalEnvironment() == [FOO: "BAR"]
    }
}
