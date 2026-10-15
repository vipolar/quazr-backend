# Quazr Quizr Instances (QQI)

### **QQI Controller** Design Documentation

#### **General Patterns:**
* Instance identifier *(quizrID, instanceUUID)* is received as required path variables - */quizrID/instanceUUID* to be passed on to the **QQI Service** for processing.
* Data is received as a *Java Record (missing/null fields allowed) Model* or as a set of *explicitly defined request parameters* and passed on to the **QQI Service** for processing.
* All requests should contain an *Authentication Token* or a *Temporary ID Token* to identify the author of the request and should be checked thoroughly (except for the *instance creation* request, where temporary authentication token will be issued if no token was presented upon request!)
* All exceptions thrown by the methods called by **QQI Controller** bubble up to the **QQI Controller** layer and are used to construct appropriate *ResponseEntity<?>* returns.

#### **Mappings** *(path = "/instances")***:**
* ***@PostMapping("/{quizrID}/new")*** - Create a new quizr instance (requires *quizrID* path variable) and return the newly created *instance UUID* or an appropriate error message on exception.
* ***@GetMapping("/{quizrID}/{instanceUUID}")*** - Retrieve and return existing Quizr instance (requires *quizrID* and *instanceUUID* path variables) or an appropriate error message on exception.
* ***@PostMapping("/{quizrID}/{instanceUUID}/player/create")*** - Create a new quizr instance player (requires *quizrID* and *instanceUUID* path variables and a *nickname* variable,) append it to the *instance players array* and return the newly created *playerUUID* or an appropriate error message on exception.
* ***@PostMapping("/{quizrID}/{instanceUUID}/player/edit")*** - Edit an existing quizr instance player (requires *quizrID* and *instanceUUID* path variables and *playerRecord* object) and return the same *playerUUID* or an appropriate error message on exception.