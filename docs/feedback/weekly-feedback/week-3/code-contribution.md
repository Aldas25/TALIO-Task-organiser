# Code Contributions and Code Reviews

#### Focused Commits

Grade: **Insufficient**

Feedback: 
- For week 3 some of members don't have commits at all. Please make sure you are aware that we run our gitinspector with 24 hours before the meeting. I saw you had some commits and merge requests on the very last moment and for this time I will still consider them. Since I am giving this feedback late, I will also give examples from more recent commits.
- The commits that you have are having a good amount of code and a clear target. An example of this is commit 89a26450, where even the commit message is pretty good. The description of what the commit is doing is usually very general. You could try formulate them like (This commit) "Adds API Endpoints (deleting and updating Cards and CardLists)"
- As a **TIP** try to make sure everybody has meaningful commits related to small individual changes. Your commit messages should be descriptive but still concise. Always document your code in time. You should not commit just comments or checkstyle changes too often. Also make sure your commits are tested and run the build before, I see you have a failing pipeline most of the time. Also, in your commits try to have as little commented code as possible.


#### Isolation

Grade: **Sufficient**

Feedback:
- You started working multiple features on small isolated branches. This is good (**TOP**) since you should use feature branches for your work and make MR targeted directly to main. As a TA I am supposed to look in general at the MR you perform to main.
- As a **TIP** try to make sure you name your branches descriptive per feature or per change. A bad example is MR 10 because it lacks description, the name of the feature branch is not descriptive enouth. What is exactly basic functionality? Could you relate that to the user story or epic name? In the description you could make a list of things added by this MR (e.g controller for basic cards functionality, UI for displaying a cards, etc). I also recommend to test + document the code before merging it.  Moreover, that MR adds a lot of functionality. Try to really isolate the commits and merge small. Please do not name the branches with your name.
- As an example MR 13 has a better description and name. Maybe try to test the code as it goes. This means do not test code at the end because the commits should in general be tested and documented.

#### Reviewability

Grade: **Insufficient**

Feedback:
- Your MR are usually not that reviewable since they lack descriptions (constructive ones), making it hard for the reviewers to understand what changes are introduced by the MR.
- As a **TOP** follow the idea of makinf descriptions in your MR like in MR 13.
- As a **TIP**, try to make the MR clear, focused, with good descriptions so they are easy to review. The changes should not be related to other features and always solve merge conflicts. Please always let other students approve your MR, do not approve your own!!
- As a good example, check some of Aldas reviews on MR 13. I want to see more of you involved in disucssions.


#### Code Reviews

Grade: **Insufficient**

Feedback:
- For week 3 there was little or no reviews. Note that a comment of "nice work" or "looks good to me" are not constructive reviews.  
- As a **TIP** try to make sure everybody reviews at least 1 MR in a constructive way. Keep the discussions on point and always involve more members in more controversial changes. Do not leave your MR open for too long. Merge small and often so the code is checked frequently. When performing a MR always respond to the reviews given by your collegues.



#### Build Server

Grade: **Sufficient**

Feedback:
- This part is related to the building of the Server, and you run the server often, but has a lot of fails.
- As a **TOP** you added your checkstyle rules. Make sure you not only use the ones from oop, but come up with your own. (at least 10)
- As a **TIP** make sure you commit frequently so the pipeline runs often. You should not have a failing pipline especially on main. In case of a failing pipline fix it as soon as possible and test your code by building it before pushing. Make sure your checkstyle rules are followed (run it before pushing), since they can also lead to failing pipelines. You should not have commits that change the formating too often (so basically repairing the checkstyle you forgot to run). Make sure you choose your own checkstyle rules.

