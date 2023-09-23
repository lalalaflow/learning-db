package dev.helloworld.jdbc.service;

import dev.helloworld.jdbc.domain.Member;
import dev.helloworld.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false);
            bizLogic(con, fromId, toId, money);

            con.commit();
        }catch (Exception e) {
            con.rollback();;
            log.error("error", e);
            throw new IllegalStateException();
        }finally {
            release(con);
        }

    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toId);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

    private static void release(Connection con) {
        if(con != null) {
            try {
                con.setAutoCommit(true);
                con.close();
            }catch (Exception e){
                log.error("error when closing connection", e);
            }
        }
    }

    private static void validation(String toId) {
        if(toId.equals("ex")){
            throw new IllegalStateException("이체 중 예외발생");
        }
    }
}
