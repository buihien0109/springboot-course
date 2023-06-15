## Lambda expresstion là gì?

**Lambda Expressions** (Biểu thức Lambda) là một trong những tính năng mới được giới thiệu trong Java 8.

**Lambda Expression** là một hàm không có tên(hàm ẩn danh) và không thuộc bất kỳ lớp nào, không có phạm vi truy cập (private, public hoặc protected), không khai báo kiểu trả về

**Lambda Expression** không tự thực thi mà thay vào đó nó được sử dụng để thực thi một phương thức được định nghĩa trong functional interface.

## Tại sao sử dụng Lambda expresstion

- Cung cấp bản implement cho Functional interface.
- Viết ít code hơn.
- Hiệu quả hơn khi làm việc với các Collection và Stream API.

## Cú pháp

Biểu thức Lambda trong java gồm có 3 thành phần sau:

```
(argument-list) -> {body}
```

Trong đó:

- `Argument-list`: danh sách tham số (có thể không có, có một hoặc nhiều tham số)
- `Arrow-operator`: toán tử mũi tiên được sử dụng để liên kết danh sách tham số và body của biểu thức.
- `Body`: nội dung thực thi, là 1 khối lệnh hoặc 1 biểu thức.

## Ví dụ

**1. Không có tham số**

```java
@FunctionalInterface
interface Hello {
    void sayHello();
}

public class LambdaExpression {
    public static void main(String[] args) throws Exception {
        Hello h = () -> {
            System.out.println("Xin chào các bạn");
        };

        h.sayHello();
    }
}
```

**2. Có 1 tham số**

```java
@FunctionalInterface
interface Hello {
    void sayHello(String name);
}

public class LambdaExpression {
    public static void main(String[] args) throws Exception {
        Hello h = name -> {
            System.out.println("Xin chào " + name);
        };

        h.sayHello("Bùi Hiên");
    }
}

```

**3. Có 2 tham số**

```java
@FunctionalInterface
interface Hello {
    void sayHello(String name, int year);
}

public class LambdaExpression {
    public static void main(String[] args) throws Exception {
        Hello h = (name, year) -> {
            System.out.println("Xin chào các bạn. Tôi tên là " + name + ", năm nay tôi " + (2022 - year) + " tuổi.");
        };

        h.sayHello("Bùi Hiên", 1997);
    }
}

```

## So sánh với việc triển khai interface mà không có lambda expression

**Cách 1: Tạo một class mới**

```java
@FunctionalInterface
interface Hello {
    void sayHello(String name);
}

public class App implements Hello{
    public static void main(String[] args) throws Exception {
        Hello h = new App();
        h.sayHello("Bùi Hiên");
    }

    @Override
    public void sayHello(String name) {
        System.out.println("Xin chào " + name);
    }
}

```

**Cách 2: Sử dụng anonymous class**

```java
@FunctionalInterface
interface Hello {
    void sayHello(String name);
}

public class LambdaExpression{
    public static void main(String[] args) throws Exception {
        Hello h = new Hello() {
            @Override
            public void sayHello(String name) {
                System.out.println("Xin chào " + name);
            }
        };
        h.sayHello("Bùi Hiên");
    }
}
```