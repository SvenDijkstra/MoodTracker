# **Code Constants: Simple Mood Tracker (Aura)**

This file defines all static, non-changing constants required for the application's logic, UI, and data handling. These values should typically be implemented as companion object constants in Kotlin or static final fields in Java.

## **1\. Mood Scale Constants**

| Constant Name | Value | Mood Label (UI-02, UI-07) | Description |
| :---- | :---- | :---- | :---- |
| MOOD\_VERY\_SAD | 0 | "Very Sad" | Extreme Negative |
| MOOD\_SAD | 1 | "Sad" | Mild Negative |
| MOOD\_NEUTRAL | 2 | "Neutral" | Baseline |
| MOOD\_HAPPY | 3 | "Happy" | Mild Positive |
| MOOD\_VERY\_HAPPY | 4 | "Very Happy" | Extreme Positive |

## **2\. Severity Constants (LOGIC-01)**

| Constant Name | Value | Unit | Notes |
| :---- | :---- | :---- | :---- |
| SEVERITY\_MIN | 1 | Integer | Minimum severity for a simple tap. |
| SEVERITY\_MAX | 10 | Integer | Maximum severity achievable (at 5.0s hold). |
| HOLD\_DURATION\_MAX\_SECONDS | 5.0f | Float | Maximum time to track hold event. |
| TAP\_THRESHOLD\_SECONDS | 0.5f | Float | Duration below which an event is considered a tap. |
| SEVERITY\_MULTIPLIER | 2.0f | Float | seconds\_held \* 2 |

## **3\. Graph Filter Presets (UI-09)**

The key should be used for storage (last\_filter\_key), and the label for the UI button text. Values are in milliseconds.

| Key | Label | Millisecond Value |
| :---- | :---- | :---- |
| "8\_hours" | "8 Hours" | 8×60×60×1000 |
| "12\_hours" | "12 Hours" | 12×60×60×1000 |
| "24\_hours" | "24 Hours" | 24×60×60×1000 |
| "1\_week" | "1 Week" | 7×24×60×60×1000 |
| "1\_month" | "1 Month" | 30×24×60×60×1000 |
| "1\_year" | "1 Year" | 365×24×60×60×1000 |

## **4\. Aesthetic & Color Palette (UI-13)**

The palette aims for a calming, sterile light theme using cool tones. Colors are defined in hex codes.

| Constant Name | Hex Code | Usage |
| :---- | :---- | :---- |
| COLOR\_PRIMARY\_BLUE | \#007BFF | Main action buttons, active navigation indicator. |
| COLOR\_BACKGROUND\_LIGHT | \#F8F9FA | Main screen background (subtly off-white). |
| COLOR\_SURFACE\_WHITE | \#FFFFFF | Card backgrounds, containers. |
| COLOR\_ACCENT\_GREEN | \#28A745 | Positive feedback, growth in graph. |
| COLOR\_NEGATIVE\_RED | \#DC3545 | Negative feedback, decline in graph. |
| COLOR\_NEUTRAL\_GREY | \#6C757D | Secondary text, neutral smiley. |
| COLOR\_SHAKE\_GROWTH | \#FFC107 | Visual feedback for the 'hold' animation (A pulsing yellow/orange). |

