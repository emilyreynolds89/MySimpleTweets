# Project 3 - MySimpleTweets

MySimpleTweets is an android app that alllows users to connect to their Twitter account and use Twitter features within the app.

Submitted by: Emily Reynolds

Time spent: 22 hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can sign in to Twitter using OAuth login
* [x] User can view the tweets from their home timeline 
* [x] User can compose a new tweet

The following **stretch** features are implemented:

* [x] While composing a tweet, user can see a character counter with characters remaining for tweet out of 280
* [x] User can refresh timeline of tweets by pulling down to refresh
* [x] Improve the user interface and theme the app to feel "twitter branded" with colors and styles
* [x] When any background or network task is happening, user sees an indeterminate progress indicator 
* [x] User can "reply" to any tweet from their home timeline
* [x] User can click on a tweet to be taken to a "detail view" of that tweet
* [x] User can take favorite (and unfavorite) or reweet actions on a tweet
* [x] User can see embedded image media within a tweet on list or detail view
* [x] User can view more tweets as they scroll with infinite pagination. Number of tweets is unlimited. Refer to the infinite pagination conceptual guide for more details
* [ ] Compose activity is replaced with a modal overlay
* [x] Links in tweets are clickable and will launch the web browser
* [ ] Use Parcelable instead of Serializable leveraging the popular Parceler library
* [x] Replace all icon drawables and other static image assets with vector drawables where appropriate
* [x] User can view following / followers list through any profile they view
* [ ] Apply the popular ButterKnife annotation library to reduce view boilerplate
* [ ] Experiment with fancy scrolling effects on the Twitter profile view
* [ ] User can open the twitter app offline and see last loaded tweets persisted into SQLite

The following **additional** features are implemented:

* [ ] List anything else that you can get done to improve the app functionality!

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/link/to/your/gif/file.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

Unable to record a walkthrough of implemented user stories - app would not run on emulator, only of Android phone synced to laptop.

## Notes

Describe any challenges encountered while building the app.

I had challenges implementing some of the stretch features. Some of the more diffifcult ones included incorporating 
fully functional favorite & retweet buttons, and the infinite pagination feature.

## License

    Copyright [yyyy] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
