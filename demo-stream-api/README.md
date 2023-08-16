## Stream là gì?

Stream (luồng) là một đối tượng mới của Java được giới thiệu từ phiên bản Java 8, giúp cho việc thao tác trên collection và array trở nên dễ dàng và tối ưu hơn.

Một Stream đại diện cho một chuỗi các phần tử hỗ trợ các hoạt động tổng hợp tuần tự (sequential) và song song (parallel).

Tất cả các class và interface của Stream API nằm trong gói java.util.stream. Bằng cách sử dụng các stream, chúng ta có thể thực hiện các phép toán tổng hợp khác nhau trên dữ liệu được trả về từ các collection, array, các hoạt động Input/Output

## Các tính chất của Stream

* Stream không phải một cấu trúc dữ liệu, đầu vào của Stream có thể là các collections(ArrayList, Set, LinkedList,…), Arrays và các kênh input/output
* Stream không làm thay đổi dữ liệu gốc mà chỉ trả về kết quả thông qua các methods
* Về cơ bản các method của Stream được chia thành 2 loại là hoạt động trung gian (intermediate Operation) và hoạt động đầu cuối (Terminal Operation)


## Terminal Operations

**Phương thức collect()**: Dùng để trả về kết quả của Stream dưới dạng List hoặc Set
```java
List<String> strings = Arrays.asList("args", "", "code", "learn", "...");
List<String> filter = strings.stream().collect(Collectors.toList());
System.out.println(filter);
```
Kết quả trả về:
```
[args, , code, learn, ...] 
```

<br>

**forEach()**: dùng để duyệt mọi phần tử trong Stream
```java
List<String> strings = Arrays.asList("args", "", "code", "learn", "...");
strings.stream().forEach(s -> System.out.println(s));
```
Kết quả trả về
```
args

code 
learn 
... 
```
<br>

**reduce()**: reduce() method với 1 trong 2 tham số truyền vào là method reference, dùng để kết hợp các phần tử thành một giá trị đơn cùng kiểu với dữ liệu ban đầu
```java
List<String> strings = Arrays.asList("args", "", "code", "learn", "...");
String result = strings.stream().reduce("-", String::concat);
System.out.println(result);
```
Tham số đầu tiên là chỉ giá trị ban đầu, tham số thứ 2 là một method reference String::concat nhằm mục đích ghép các phần tử của Stream với nhau
```
-argscodelearn... 
```
<br>

**max(), min()**: Trả về giá trị lớn nhất hoặc bé nhất trong các phần tử
```java
List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
Integer maxx = list.stream().max(Integer::compare).get();
Integer minn = list.stream().min(Integer::compare).get();
System.out.println("Max: "+maxx);
System.out.println("Min: "+minn);
```
Kết quả trả về:
```
Max: 10 
Min: 1 
```

## Intermediate Operations

**distinct()**: dùng để loại bỏ các phần tử trùng lặp
```java
 List<Integer> list = Arrays.asList(1,2,2,2,2,3,4,5);
 list.stream().distinct().forEach(System.out::println);
```
Kết quả trả về:
```
1 
2 
3 
4 
5 
```
<br>

**map()**: dùng để trả về một Stream mà ở đó các phần tử đã được thay đổi theo cách người dùng tự định nghĩa
```java
List<Integer> list = Arrays.asList(1,2,2,2,2,3,4,5);
list.stream()
.distinct()
.map(i -> i*i)
.forEach(System.out::println);
```
Kết quả trả về:
```
1 
4 
9 
16 
25 
```

**filter()**: dùng để lọc và xóa bỏ các phần tử với điều kiện do người dùng định nghĩa
```java
List<Integer> list = Arrays.asList(1,2,2,2,2,3,4,5);
list.stream()
.distinct()
.filter(i -> i > 2)
.forEach(System.out::println);
```
Kết quả trả về:
```
3 
4 
5 
```

<br>

**sorted()**: dùng để sắp xếp các phần tử
```java
List<Integer> list = Arrays.asList(4,3,2,1,0,3,4,5);
list.stream().sorted().forEach(System.out::println);
```
Kết quả trả về:
```
0 
1 
2 
3
3 
4
4 
5 
```

<br>

**limit(n)**: với tham số truyền vào là số nguyên không âm n, nó sẽ trả về một stream chứa n phần tử đầu tiên
```java
List<Integer> list = Arrays.asList(4,3,2,1,0,3,4,5);
list.stream().limit(3).forEach(System.out::println);
```
Kết quả trả về:
```
4 
3 
2 
```

<br>  

**skip(n)**: với tham số truyền vào là một số không âm n, nó sẽ trả về các phần tử còn lại đằng sau n phần tử đầu tiên

```java
List<Integer> list = Arrays.asList(4,3,2,1,0,3,4,5);
list.stream().skip(3).forEach(System.out::println);
```

Kết quả trả về:
```
1 
0 
3 
4 
5 
```