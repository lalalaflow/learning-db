package dev.helloworld.jdbc.service;

import dev.helloworld.jdbc.domain.Member;
import dev.helloworld.jdbc.repository.MemberRepositoryV2;
import dev.helloworld.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 매니저
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {

    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            bizLogic(fromId, toId, money);
            transactionManager.commit(status);

        }catch (Exception e) {
            transactionManager.rollback(status);
            log.error("error", e);
            throw new IllegalStateException();
        }
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toId);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private static void validation(String toId) {
        if(toId.equals("ex")){
            throw new IllegalStateException("이체 중 예외발생");
        }
    }
}
