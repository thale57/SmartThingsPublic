/**
 *  Say Hi When I Get Home
 *
 *  Copyright 2014 Tracy Hale
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Say Hi When I Get Home",
    namespace: "",
    author: "Tracy Hale",
    description: "House will greet me when I get home",
    category: "My Apps",
    iconUrl: "https://graph.api.smartthings.com/api/devices/icons/st.Home.home2-icn",
    iconX2Url: "https://graph.api.smartthings.com/api/devices/icons/st.Home.home2-icn?displaySize=2x",
    iconX3Url: "https://graph.api.smartthings.com/api/devices/icons/st.Home.home2-icn?displaySize=2x")


preferences {
	section("Choose sensors to monitor") {
		input "door", "capability.contactSensor", title: "Door contact sensor", required: true
        input "motion", "capability.motionSensor", title: "Motion sensor", required: true
        //input "sonos", "capability.musicPlayer", title: "Sonos Device", required: true
        input "message", "text", title: "Message Sonos will say", required: true
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	initialize()
}

def initialize() {
	unsubscribe()
    awaitDoorOpen()
}

def awaitDoorOpen() {
	log.debug "awaiting door open"
    
	subscribe(door, "contact.open", contactOpenHandler)
}

def contactOpenHandler(evt) {
	log.debug "contact $evt.value"
    
    subscribe(motion, "motion.active", motionAfterDoorOpen)
    subscribe(door, "contact.closed", contactCloseHandler)
}

def contactCloseHandler(evt) {
	log.debug "contact $evt.value"
    
    initialize()
}

def motionAfterDoorOpen(evt) {
	log.debug "send message ${message}"
    
    //sonos.playText(message)
    initialize()
}
    
    
    
    
    
    