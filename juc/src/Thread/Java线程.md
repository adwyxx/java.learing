## Java线程
### 一、什么是线程

#### 2.线程的生命周期
- new : 创建一个线程
- runable : new Thread()之后，调用run()方法之前，线程是可执行的
- running : 运行状态，调用run()方法之后获取CPU资源
- block : 线程由于某种原因被阻塞执行时
- terminate : 线程执行完毕或因为其他原因中断执行后的状态
```
graph LR
    nt[newThread] --start--> runable
    runable --run--> running
    running --> block
    block --> terminate
    running --> terminate
    runable --> terminate
    
```
### 二、线程的创建方式
#### 1.new Thread();

#### 2.实现Runable接口
#### 3.实现Callable接口
