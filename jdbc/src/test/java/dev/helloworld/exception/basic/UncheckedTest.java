package dev.helloworld.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class UncheckedTest {

    @Test
    void unchecked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_throw() {
        Service service = new Service();
        //service.callThrow();
        assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyUncheckedException.class);
    }

    static class Service {
        Repository repository = new Repository();
        public void callCatch() {
            try {
                repository.call();
            }catch (MyUncheckedException e) {
                log.info("예외처리, message={}", e.getMessage(), e);
            }
        }


        public void callThrow() {
            repository.call();
        }
    }

    static class Repository  {
        public void call() {
            throw new MyUncheckedException("ex");
        }
    }

    /**
     * RuntimeException 을 상속받은 예외는 언체크예외가 된다.
     */
    static class MyUncheckedException extends RuntimeException{

        public MyUncheckedException(String message) {
            super(message);
        }
    }
}
