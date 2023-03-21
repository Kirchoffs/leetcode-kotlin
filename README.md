# leetcode-kotlin

## Run
```
>> kotlinc LC-2571.kt -include-runtime -d LC-2571.jar
>> java -jar LC-2571.jar
>> rm LC-2571.jar
```

or use the script run.sh

```
>> chmod +x run.sh
>> run.sh 2571
>> run.sh 2457
```

## Kotlin Notes
### IntArray
```
val nums = intArrayOf(1, 2, 3)
```

### For Loop
```
for (i in 1 until nums.size) {
    ...
}
```