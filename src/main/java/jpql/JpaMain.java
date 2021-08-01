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

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
