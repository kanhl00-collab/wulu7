# Group 14

Adding Floating-Point Numbers to P0

Members:
- Fang Ye
- Hailan Kan
- Samuel Alderson

1 File demonstration:
- All of the projects source code, tests, and documentation are in the directory **src**
- Our research domain is defined by code additions to the four modules ST, SC, P0, and CGwat originally drawn from lab 10. 
- The unit testing files STTest.ipynb, SCTest.ipynb, P0Test.ipynb, and CGwatTest.ipynb correspond to the four modules mentioned above. 
- IntegrationTest.ipynb is an integration testing file.

2 Implementation:
- 1. Add two new types: single-precision floating-point number(float32) and double-precision floating-point number(double64)
- 2. Add implicit type conversion between float, double, and integer types for assignments, binary operations and relations. Add explicit type conversions.
- 3. Add Unicode symbol operations for square root, floor, ceiling, and absolute value.
- 4. Add 'max' and 'min' infix operators
- 5. Add 'sum' and 'max' quantified expressions for arrays: sum(a[i..j]), max(a[i..j])

3 Documentation:
- The necessary modifications have been documented at the top of each module. 
- For any significant modifications, code comments and an additional markdown cell above the code will explain the changes. 
- A summary at the top of each testing file states the file purpose and the total number of test cases. For each test case, the expected result is the documented above in markdown.

4 Testing:
- Unit testing: STTest.ipynb, SCTest.ipynb, P0Test.ipynb, CGwatTest.ipynb
- Integration testing: IntegrationTest.ipynb

5 Compile and Run:
- To run the test cases, execute both of the runwasm and runpywasm methods first.  
- Running the test cases in the IntegrationTest file will demonstrate our implementation and verify our results.  
- In total, there are 56 integration test cases for all 5 parts of implementation. You can access the test cases for each part by clicking the text at the top of the file or through the Table of Contents. 