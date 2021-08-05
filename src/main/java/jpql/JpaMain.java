package jpql;

import javax.persistence.*;
import java.util.List;

class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

/* 반환타입, 프로젝션
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            // 반환 타입에 정함이 있는 쿼리, 미정인 쿼리
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            TypedQuery<Member> query2 = em.createQuery("select m from Member m where m.id = 1", Member.class);
            Query query3 = em.createQuery("select m.username, m.age from Member m");

            // 반환 개수에 따른 메소드
            List<Member> resultList = query1.getResultList();
            Member result = query2.getSingleResult(); // 없거나 둘 이상이면 exception 남

            // 이름 지정 파라미터 세팅
            Member query4 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                                .setParameter("username", "member1")
                                .getSingleResult();
            System.out.println("query4.getUsername() = " + query4.getUsername());


            em.flush();
            em.clear();

            // 엔티티 프로젝션1
            List<Member> query5 = em.createQuery("select m from Member m", Member.class)
                    .getResultList();   // select 결과값이 전부 영속성 컨텍스트에서 관리됨

            Member findMember = query5.get(0);
            findMember.setAge(20);


            // 엔티티 프로젝션2
            List<Team> query6 = em.createQuery("select m.team from Member m", Team.class).getResultList();
            // select문은 join으로 나감 따라서 한 눈에 볼 수 있고 성능 문제가 없도록 명시적으로 join 작성해서 날리자
            // select m.team from Member m join m.team t


            // 임베디드 타입 프로젝션
            em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();


            // 스칼라 타입 프로젝션 (Object[] 타입으로 조회)
            List<Object[]> query7 = em.createQuery("select m.username, m.age from Member m")
                    .getResultList();

            Object[] obj = query7.get(0);
            System.out.println("username = " + obj[0]);
            System.out.println("age = " + obj[1]);
            // 타입이 명시되어 있지 않기 때문에 컬럼을 순서대로 Object 배열에 넣어서 반환함


            // 스칼라 타입 프로젝션 (new 명령어로 조회)
            List<MemberDTO> query8 = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            MemberDTO memberDTO = query8.get(0);
            System.out.println("memberDTO = " + memberDTO.getUsername());
            System.out.println("memberDTO = " + memberDTO.getAge());
*/


/* 페이징
            for(int i=0; i<100; i++){
                Member member = new Member();
                member.setUsername("member"+i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            List<Member> result = em.createQuery("select m from Member m order by m.age asc", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result.size() = " + result.size());
            for (Member member1 : result) {
                System.out.println("member1 = " + member1);
            }
*/


/* Join절
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

//            String query = "select m from Member m join m.team t";                // inner join
//            String query = "select m from Member m left join m.team t";           // left outer join
//            String query = "select m from Member m, Team t where m.username = t.name";        // theta join
//            String query = "select m from Member m left join m.team t ON t.name = 'A'";       // ON절 활용한 '조인 대상 필터링'
            String query = "select m from Member m left join Team t ON m.username = t.name";    // ON절 활용한 '연관관계 없는 엔티티 외부 조인'

            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();
*/


/* enum 타입
            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            em.persist(member);

            em.flush();
            em.clear();

            String query = "select m.username, 'HELLO', true from Member m " +
                            "where m.type = :userType";     // 쿼리문에 enum 넣을 때는 패키지까지 다 써넣음

            List<Object[]> result = em.createQuery(query)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();

            Object[] obj = result.get(0);
            System.out.println("username = " + obj[0]);
            System.out.println("HELLO = " + obj[1]);
            System.out.println("true = " + obj[2]);
*/


/*
            Member member = new Member();
            member.setUsername("관리자");
            member.setAge(10);

            em.persist(member);

            em.flush();
            em.clear();

            // CASE 식
            String query1 = "select " +
                            " case when m.age <= 10 then '학생요금' " +
                            "      when m.age >= 60 then '경로요금' " +
                            "      else '일반요금' " +
                            " end " +
                            "from Member m";

            // coalesce
            String query2 = "select coalesce(m.username, '이름 없는 회원') from Member m ";

            String query3 = "select nullif(m.username, '관리자') as username " +
                            "from Member m";

            List<String> result = em.createQuery(query3, String.class)
                    .getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }
*/

/*

            Member member1 = new Member();
            member1.setUsername("관리자1");
            em.persist(member1);
            
            Member member2 = new Member();
            member2.setUsername("관리자2");
            em.persist(member2);

            em.flush();
            em.clear();

            // concat
            String query1 = "select 'a' || 'b' from Member m";

            // substring
            String query2 = "select substring(m.username, 2, 3) from Member m";

            // locate
            String query3 = "select locate('de', 'abcdefg') from Member m";

            // size
            String query4 = "select size(t.members) from Team t";   // 컬렉션의 크기 반환

            // 사용자 정의 함수 (dialect 상속 클래스 정의, persistence.xml에 상속받은 방언 등록, 사용)
            String query5 = "select function('group_concat', m.username) from Member m";


           List<String> result5 = em.createQuery(query5, String.class)
                    .getResultList();

            List<Integer> result2 = em.createQuery(query3, Integer.class)
                    .getResultList();

            for (String s : result5) {
                System.out.println("s = " + s);
            }

            for (Integer i : result2) {
                System.out.println("i = " + i);
            }
*/


/* 경로표현식 (묵시적 조인은 실무에서 X)
            Member member1 = new Member();
            member1.setUsername("관리자1");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            em.persist(member2);

            em.flush();
            em.clear();

            // 상태 필드(state field)
            String query1 = "select m.username from Member m";

            // 단일 값 연관 경로 (묵시적 내부 조인(inner join) 발생, 탐색O) - 실무에서는 사용 지양
            String query2 = "select m.team.name from Member m";

            // 컬렉션 값 연관 경로 (묵시적 내부 조인(inner join) 발생, 탐색X) - 실무에서는 사용 지양
            String query3 = "select t.members from team t";


            List<String> result = em.createQuery(query2, String.class)
                    .getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }
*/

/* 페치 조인

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            // 페치 조인
            String query1 = "select m from Member m join fetch m.team";
            String query2 = "select t from Team t join fetch t.members";    // member 수만큼 t의 결과 row수가 결정됨
            String query2_1 = "select t from Team t join t.members";        // 일반 join의 경우 select절에 있는 t 엔티티만 가져옴
            String query3 = "select distinct t from Team t join fetch t.members";    // jpql의 distinct는 sql뿐 아니라 엔티티의 중복도 제거해 줌

            // 페치 조인의 한계
            String query4 = "select t from Team t join fetch t.members as m";                // 페치 조인 대상 엔티티는 별칭 X
            String query5 = "select t from Team t join fetch t.members join fetch t.orders"; // 둘 이상의 컬렉션은 페치 조인 X
            String query6 = "select t from Team t"; // 페이징 못 함. BatchSize 세팅으로 해결.
            // team 한 번 가져오고 batch size만큼 team_id 파라미터 세팅해서 in절로 member 한 방 쿼리해 옴.

            List<Team> result = em.createQuery(query3, Team.class)
                    .getResultList();

            List<Team> result2 = em.createQuery(query6, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            for (Team team : result) {
                System.out.println("member = " + team.getMembers().size() + ", team = " + team.getName());
            }

            for (Team team : result2) {
                System.out.println("team = " + team.getName() + "|members=" + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("-> member = " + member);
                }
            }
*/

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            // named 쿼리
            List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();

            for (Member member : resultList) {
                System.out.println("member = " + member);
            }


            // 벌크 연산
           int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate(); // 자동 flush

            System.out.println("resultCount = " + resultCount);
            // 주의사항
            // 벌크 연산은 영속성 컨텐스트를 무시하고 DB에 직접 쿼리
            // 방법1: 벌크 연산을 먼저 실행
            // 방법2: 벌크 연산 후 영속성 컨텍스트 초기화

            System.out.println("member1.getAge() = " + member1.getAge());
            System.out.println("member2.getAge() = " + member2.getAge());
            System.out.println("member3.getAge() = " + member3.getAge());
            // 영속성 컨텍스트에 있는 값이 그대로 나오기 때문에 나이가 0으로 나옴
            // 그런데, 다시 member를 조회해도 안 됨.
            // 왜냐하면 처음 persist할 때 pk값이 1차 캐시에 들어갔기 때문에 DB만 반영된 update 내역을 영속성 컨텍스트는 모름.
            // 따라서 초기화 후 다시 조회해와야 함.


            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
