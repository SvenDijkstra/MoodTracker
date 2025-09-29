# **Data Export File Specifications**

This document defines the output format and content for the raw data export functionality (FEAT-05). The data source is the MoodLog entity defined in the tech\_spec.md.

## **1\. Common Data Fields**

All export formats must contain the following three fields for every logged entry:

1. **timestamp**: UTC epoch time in **milliseconds**. (e.g., 1759144200000\)  
2. **mood\_value**: The raw numerical mood value (0, 1, 2, 3, or 4).  
3. **severity**: The intensity score (1 to 10).

## **2\. CSV Format (.csv)**

The CSV (Comma Separated Values) export must include a header row and use the comma (,) as the field delimiter.

### **2.1 CSV Structure**

| Column 1 | Column 2 | Column 3 |
| :---- | :---- | :---- |
| timestamp | mood\_value | severity |

### **2.2 CSV Example Output**

timestamp,mood\_value,severity  
1759144200000,3,6  
1759145600000,0,10  
1759147500000,2,1

## **3\. JSON Format (.json)**

The JSON export must be a single array of objects, where each object represents one MoodLog entry.

### **3.1 JSON Structure**

\[  
  {  
    "timestamp": 1759144200000,  
    "mood\_value": 3,  
    "severity": 6  
  },  
  {  
    "timestamp": 1759145600000,  
    "mood\_value": 0,  
    "severity": 10  
  },  
  {  
    "timestamp": 1759147500000,  
    "mood\_value": 2,  
    "severity": 1  
  }  
\]

## **4\. TXT Format (.txt)**

The TXT export must be a simple, human-readable format. It should include the headers and use a pipe (|) or tab character as the delimiter. Using a pipe is generally preferred for simple TXT files to avoid conflicts with comma-separated numbers.

### **4.1 TXT Structure**

Must include headers on the first line, separated by tabs or pipes. We will use a pipe (|) for clarity.

timestamp|mood\_value|severity  
1759144200000|3|6  
1759145600000|0|10  
1759147500000|2|1  
