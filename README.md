# gerard.jacolbia
for planit takehome exam

# PLEASE READ
I've specified in my application (first written, and then the one-way video) that I could not easily migrate to AU as I am the only person taking care of my father.
Just a heads up that this could cause an issue on my application down the line so just that everyone would be aware of this. Since I've reached this stage I would assume
that this is not much of an issue for PlanIt, but again in case this was not clear, I am highlighting it again now.

# Install
1. Clone project.
2. In root of project,

    For mac/linux

    > ./gradlew runAllTests
    
    For windows

    > gradlew.bat runAllTests
3. Find reports in /build/reports/tests/runAllTests/index.html 

# IDE Setup
1. Use IntelliJ
2. Clone project and refresh gradle.

# Answer to Questions
## 1. What other possible scenario’s would you suggest for testing the Jupiter Toys application?
* Navigation
    * all links leads to correct pages
* Home Page
    * banner is correct
* Shop Page
    * Test products offered, i.e. product name, and product prices offered are correct
    * Buying a product updates the Cart item count in navigation bar
    * All expected products are offered
* Contact Page
    * Only valid inputs should be allowed (correct format, test for xss attempts, sqli, directory traversals, etc)
    * Message received correctly, either check in db via api, or sql script triggered for assertion
* Login Page
    * Only valid inputs should be allowed (correct format, test for xss attempts, sqli, directory traversals, etc)
    * Error messages
    * Brute force/rate limiting tests
* Cart Page
    * Quantity field should be tested when updated, and sub-total, total and cart item total should be updated too
    * Deleting items on cart, total should be updated too
    * If all items are deleted, empty shopping cart page displayed
    * Rounding of values for Sub-total and total
    * Emptying the cart would empty it, and the cart item total should revert to 0
* Checkout Page
    * All required fields fieled up to proceed
    * Only valid inputs should be allowed (correct format, test for xss attempts, sqli, directory traversals, etc)
    * Order should only be processed via valid form, i.e. should not be triggered by sending POST request manually bypassing ui validation
    * Cart items number should be consistent with order products
    * Order number returned in UI

## 2. Jupiter Toys is expected to grow and expand its offering into books, tech, and modern art. We are expecting the of tests will grow to a very large number.

    What approaches could you used to reduce overall execution time?
    * Run tests in parallel
    * Make tests data driven so tests could be re-used
    * Use API if available to set the state so no need to go through whole end to end flow. e.g. If process is A -> B -> C -> D, and API is available to set state to C, use it so no need to go through A and B.
    How will your framework cater for this?
    * Run tests in parallel, data driven -- framework is using testng, so just use a dataprovider and set parallel to true. But need refactoring of BaseTest class since WebDriver instance is not threadsafe.
    * Use API... -- framework no support for API calls. But I think possible to add, and refactor page objects/components. Since it uses LoadableComponent in a way that was not meant for it (load() method will not load
    the page, but instead wait for the page to actually load). Create own implementation similar to LoadableComponent, but use the load() method correctly i.e. it should actually load the page,
    and then have another method to wait for page to load, say waitForPage(). Call this in the get() method of the now refactored LoadableComponent class. Now if need to skip state/pages A and B, a call to C.get() would
    now allow C to actually load.

## 3. Describe when to use a BDD approach to automation and when NOT to use BDD 
I don't think there's a hard rule for this. I would just not start a project immediately on BDD. In order for BDD to be effective and capable to adapt to fast
changes, the underlying framework/glue codes should be properly designed already. That way most of the work could be just focused on the orchestration of the
step definitions.
I would suggest to start first with a data driven test with well defined interfaces. This way the team could already be productive and produce results already. Once
enough flows has been covered they could now be slowly migrated to BDD, allowing non-coders to scale up the test coverage, and the automation engineers focus on
enhancing the framework. 



