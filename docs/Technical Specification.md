# **Technical Specification: Simple Mood Tracker (Aura)**

This document provides the architectural and structural details required to implement the functional and data requirements defined in the PRD (mood\_tracker\_prd.md).

## **1\. Architecture & Persistence**

As per PERSIST-01, this application requires robust, local, on-device data storage. The recommended choice for Android is the **Room Persistence Library (SQLite abstraction)**.

### **1.1 Database Schema (Room Entity: MoodLog)**

All mood entries will be stored in a single table, mood\_entries.

| Field Name | Type (SQLite/Kotlin) | Description | Constraints / Notes |
| :---- | :---- | :---- | :---- |
| id | INTEGER (Long) | Unique primary key for the log entry. | PRIMARY KEY, AUTOINCREMENT |
| timestamp | INTEGER (Long) | The UTC epoch time (milliseconds) when the mood was logged. | NOT NULL, Used for X-Axis plotting and filtering. |
| mood\_value | INTEGER (Int) | The selected mood from the 5-point scale (0 to 4). | NOT NULL (See CODE\_CONSTANTS for mapping) |
| severity | INTEGER (Int) | The calculated intensity (1 to 10). | NOT NULL, Calculated via LOGIC-01. |

### **1.2 User Preferences (SharedPreferences or DataStore)**

To meet the persistence requirements for settings (UI-12), the following key-value pairs must be stored locally using a simple persistence mechanism (like Android's DataStore or SharedPreferences):

| Key Name | Type | Description | Default Value |
| :---- | :---- | :---- | :---- |
| last\_filter\_key | String | Stores the unique key of the last selected time preset. | "1\_week" |
| custom\_range\_start | Long | Stores the start timestamp (UTC ms) for the custom range. | null |
| custom\_range\_end | Long | Stores the end timestamp (UTC ms) for the custom range. | null |

## **2\. Core Logic Implementation**

### **2.1 Severity Calculation (LOGIC-01)**

The hold duration logic must be implemented precisely:

| Parameter | Value | Notes |
| :---- | :---- | :---- |
| **Max Hold Duration** | 5.0 seconds | The maximum time the touch event listener should track. |
| **Calculation** | Severity \= ROUND(seconds\_held \* 2\) | The final calculated value (must be capped at 10). |
| **Minimum Tap** | \< 0.5 seconds | Any touch event shorter than this duration is considered a single tap. |
| **Default Tap Severity** | 1 | If a tap is registered, severity is automatically set to 1\. |

### **2.2 Graph Aggregation (LOGIC-02)**

For all graph views covering 1 week or longer (1 Week, 1 Month, 1 Year), the data must be aggregated by the day (YYYY-MM-DD).

The database query must group entries by the day and calculate the **average** of the mood\_value for that 24-hour period.

SELECT  
    strftime('%Y-%m-%d', timestamp / 1000, 'unixepoch') AS day,  
    AVG(mood\_value) AS average\_mood  
FROM mood\_entries  
WHERE timestamp BETWEEN :start\_time AND :end\_time  
GROUP BY day  
ORDER BY day ASC

## **3\. Navigation Implementation**

As per UI-14 and UI-15, the application will utilize a single activity hosting a **ViewPager2** with a **BottomNavigationView** synced to it.

| Navigation Index | Screen Name | Bottom Nav Icon |
| :---- | :---- | :---- |
| **0** | Mood Entry | Home (Smiley/Plus Icon) |
| **1** | Visualization | Graph (Chart Icon) |
| **Swipe Gesture** | Enabled | Swiping left/right must switch between Index 0 and 1\. |

