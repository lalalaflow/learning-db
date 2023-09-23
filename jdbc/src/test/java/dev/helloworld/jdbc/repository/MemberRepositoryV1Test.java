package dev.helloworld.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import dev.helloworld.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static dev.helloworld.jdbc.connection.ConnectionConst.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 repositoryV1;

    @BeforeEach
    void beforeEach() {
        //DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setPoolName("TestPOOL");

        repositoryV1 = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException {
        Member member = new Member("memberV3", 10000);
        repositoryV1.save(member);

        Member findMember = repositoryV1.findById(member.getMemberId());
        log.info("findMember={}", findMember);

        assertEquals(member, findMember);

        repositoryV1.update(member.getMemberId(), 20000);
        Member updateMember = repositoryV1.findById(member.getMemberId());
        assertEquals(20000, updateMember.getMoney());

        // delete
        repositoryV1.delete(member.getMemberId());
        Assertions.assertThatThrownBy(() -> repositoryV1.findById(member.getMemberId())).isInstanceOf(NoSuchElementException.class);
    }



}