# Concurrency and Multithreading
Some concurrent/multithreading use cases and Java libraries utilized.

## ForkJoin
### ForkJoinTask
`ForkJoinTask` should be used for divide-and-conquer algorithms. In general when working with `ForkJoinTask`, a task is divided into 
subtasks, and each subtask is solved individually, sometimes recursively splitting into further subtasks. Finally, the results of the
 subtasks are recursively combined into a single result. Some examples include: 
* Binary search
* Merge sort
* Closest pair of points
#### RecursiveTask<T> versus RecursiveAction
Both classes are subclasses of `ForkJoinTask<T>`. The difference is that `RecursiveAction` does not return a result. 
