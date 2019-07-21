### What is `mucc`?
`mucc` is an example for interactions between Linux, Java and JavaFX.
It is a tool for purging duplicates from
a filesystem.

It does this by calculating a hash of each file
and sorting the files by hash.
If two files have the same hash, the older file will be deleted.

It also displays some information about what is happening. 
This ensures the user never suspects something went horribly wrong.


### Package Contents of `app` 

| Class         | Description |
|---------------|-------------|
| Artifacts     | Simple objects used by other classes.|
| Controller    | JavaFX class containing application logic. |
| Execute       | Issues shell commands.|
| layout.fxml   | Contains layout data.|
| Main          | Main JavaFX class. Run from here.|
| QuicksortMd5  | Quicksort algorithm.|
| README.md     | You are here.|
| Routines      | Higher level routines called by Controller.|
| Tools         | Simple tools.|
| Write         | Writes to /tmp. Used for data storage.|


### Issues and Features
- Add proper directory selection.
- Fix issues where nested duplicates would not be deleted on first pass.
- Make UI prettier.
- Make code prettier.


### Screenshot

![UI](screen.png)