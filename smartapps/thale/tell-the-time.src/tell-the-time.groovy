/**
 *  Tell the Time
 *
 *  Copyright 2016 Tracy Hale
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
    name: "Tell the Time",
    namespace: "thale",
    author: "Tracy Hale",
    description: "Sonos tells the time. There is no other text. ",
    category: "Convenience",
    iconUrl: "http://cdn.device-icons.smartthings.com/Office/office6-icn.png",
    iconX2Url: "http://cdn.device-icons.smartthings.com/Office/office6-icn@2x.png",
    iconX3Url: "http://cdn.device-icons.smartthings.com/Office/office6-icn@2x.png")


preferences {
	section {
    	input "switchButton", "capability.momentary", title: "When this button is pushed:"
		input "sonos", "capability.musicPlayer", title: "On this Sonos player", required: true
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(switchButton, "momentary.pushed", appHandler)
}

def appHandler(evt) {
	//log.debug "I got the time: ${now()}"
    Date currentDateTime = new Date()
    def currentTime = currentDateTime.format('h:mm a',TimeZone.getTimeZone('GMT-7')).toString()
    log.debug "The current time is ${currentTime}"
    state.sound = textToSpeech(currentDateTime.format('h:mm a',TimeZone.getTimeZone('GMT-7')).toString(), true)
    sonos.playTrack(state.sound.uri)
}