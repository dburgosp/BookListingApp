# Book Listing App

This is a simple Android Studio project for the [Android Basics Nanodegree](https://www.udacity.com/course/android-basics-nanodegree-by-google--nd803) given by Udacity and Google. The goal is to design and create the structure of a Book Listing app which would allow a user to get a list of published books on a given topic, using the [Google Books API](https://developers.google.com/books/) in order to fetch results and display them to the user.

# Project Specification

## Layout

1. **Overall Layout**. App contains a ListView which becomes populated with list items.
2. **List Item Layout**. List Items display at least author and title information.
3. **Layout Best Practices**. The code adheres to all of the following best practices:
   * Text sizes are defined in sp.
   * Lengths are defined in dp.
   * Padding and margin is used appropriately, such that the views are not crammed up against each other.
4. **Text Wrapping**. Information displayed on list items is not crowded.
5. **Rotation**. Upon device rotation:
   * The layout remains scrollable.
   * The app should save state and restore the list back to the previously scrolled position.
   * The UI should adjust properly so that all contents of each list item is still visible and not truncated.
   * The Search button should still remain visible on the screen after the device is rotated.

## Functionality

1. **Runtime Errors**. The code runs without errors.
2. **API Call**. The user can enter a word or phrase to serve as a search query. The app fetches book data related to the query via an HTTP request from the [Google Books API](https://developers.google.com/books/), using a class such as [HttpUriRequest](https://developer.android.com/sdk/api_diff/22/changes/org.apache.http.client.methods.HttpUriRequest.html) or [HttpURLConnection](https://developer.android.com/reference/java/net/HttpURLConnection.html).
3. **Response Validation**. The app checks whether the device is connected to the internet and responds appropriately. The result of the request is validated to account for a bad server response or lack of server response.
4. **Async Task**. The network call occurs off the UI thread using an [AsyncTask](https://developer.android.com/reference/android/os/AsyncTask.html) or similar threading object.
5. **JSON Parsing**. The JSON response is parsed correctly, and relevant information is stored in the app.
6. **ListView Population**. The [ListView](https://developer.android.com/reference/android/widget/ListView.html) is properly populated with the information parsed from the JSON response.
7. **No Data Message**. When there is no data to display, the app shows a default [TextView](https://developer.android.com/reference/android/widget/TextView.html) that informs the user how to populate the list.
8. **External Libraries and Packages**. The intent of this project is to give you practice writing raw Java code using the necessary classes provided by the Android framework; therefore, the use of external libraries for core functionality will not be permitted to complete this project.

## Code Readability

1. **Naming Conventions**. All variables, methods, and resource IDs are descriptively named such that another developer reading the code can easily understand their function.
2. **Format**. The code is properly formatted i.e. there are no unnecessary blank lines; there are no unused variables or methods; there is no commented out code. The code also has proper indentation when defining variables and methods.
