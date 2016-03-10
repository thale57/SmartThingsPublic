/**
 *  Text Demo
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
    name: "Text Demo",
    namespace: "thale",
    author: "Tracy Hale",
    description: "Just a demo to say any message when initiated from the app.",
    category: "My Apps",
    iconUrl: "https://graph.api.smartthings.com/api/devices/icons/st.People.people6-icn",
    iconX2Url: "https://graph.api.smartthings.com/api/devices/icons/st.People.people6-icn?displaySize=2x",
    iconX3Url: "https://graph.api.smartthings.com/api/devices/icons/st.People.people6-icn?displaySize=2x")


preferences {
	section("Settings") {
		input "sonos", "capability.musicPlayer", title: "Sonos Device", required: true
		input "textHere", "text", title: "Type in the message"
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
	subscribe(app, appTouchHandler)
}

def appTouchHandler(evt) {
	sonos.playText(textHere)
    //state.sound = textToSpeech(textHere, true)
    
    //sonos.playTrackAndResume(state.sound.uri, state.sound.duration)
}