# RPN Calculator
Android calculator using Reverse Polish Notation.

## Basic functionality
<img align="right" style="width: 50%; height: 50%;" src="https://user-images.githubusercontent.com/71709842/235751132-9a57aa50-f0c1-4cd5-8934-b27bff5c7a97.gif">

Calculator can perform basic mathematical operations using reverse polish notation, such as:

- addition
- subtraction
- multiplication
- division
- exponentiation
- root extraction

Negative and decimal numbers are also available.

<br><br>
## RPN-specific functionality
<img align="right" style="width: 50%; height: 50%;" src="https://user-images.githubusercontent.com/71709842/235754525-774f67eb-2634-4837-b912-06c82213bfcc.gif">

The stack shown in the upper portion of the screen is used during calculations.
There are buttons to manipulate the stack:
- DROP - drops the first number from the stack
- SWAP - swaps the first number on the stack with the second one
- AC - clears whole stack

<br><br><br><br><br><br><br>
## Additional functionality
<img align="right" style="width: 50%; height: 50%;" src="https://user-images.githubusercontent.com/71709842/235755512-3dd780f7-2f0f-4d47-bb83-6a04be53add4.gif">

Settings allow the user to:
- set the decimal precission
- change the background color
- limit the stack to 4 values (the amount that is shown on the screen)

Settings are stored in Shared Preferences and saved between sessions.
