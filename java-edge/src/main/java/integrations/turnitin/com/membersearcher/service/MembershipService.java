package integrations.turnitin.com.membersearcher.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import integrations.turnitin.com.membersearcher.client.MembershipBackendClient;
import integrations.turnitin.com.membersearcher.model.MembershipList;
import integrations.turnitin.com.membersearcher.model.User;
import integrations.turnitin.com.membersearcher.model.UserList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipService {
	@Autowired
	private MembershipBackendClient membershipBackendClient;

	/**
	 * Method to fetch all memberships with their associated user details included.
	 * This method calls out to the php-backend service and fetches all memberships,
	 * it then calls to fetch the user details for each user individually and
	 * associates them with their corresponding membership.
	 *
	 * @return A CompletableFuture containing a fully populated MembershipList object.
	 */
	// public CompletableFuture<MembershipList> fetchAllMembershipsWithUsers() {
	// 	System.out.println(membershipBackendClient.fetchUsers());
	// 	return membershipBackendClient.fetchMemberships()
	// 			.thenCompose(members -> {
	// 				CompletableFuture<?>[] userCalls = members.getMemberships().stream()
	// 						.map(member -> membershipBackendClient.fetchUser(member.getUserId())
	// 								.thenApply(member::setUser))
	// 						.toArray(CompletableFuture<?>[]::new);
	// 						// String userIdtest = Character.toString('1');
	// 						// System.out.println(membershipBackendClient.fetchUser(userIdtest));
	// 				return CompletableFuture.allOf(userCalls)
	// 						.thenApply(nil -> members);
	// 			});
	// }

	////trying with running multiple futures in parallel
	////https://www.baeldung.com/java-completablefuture
	// 	public CompletableFuture<MembershipList> fetchAllMembershipsWithUsers() {
	// 	return membershipBackendClient.fetchMemberships()
	// 			.thenCompose(members -> {
	// 				CompletableFuture<?>[] userCalls = members.getMemberships().stream()
	// 						.map(member -> membershipBackendClient.fetchUser(member.getUserId()))
	// 						.collect(Collectors.toList())
	// 						.toArray(CompletableFuture<?>[]::new);
	// 				return CompletableFuture.allOf(userCalls)
	// 						.thenApply(nil -> members);
	// 			});
	// }

	//// fetchMemberships from MembershipBackendClient.java class 	
	//// UserCall calls users into member
	//// .thenCompose (asynchronous)
	//// apply membershipBackendClient.fetchUsers() to members


	public CompletableFuture<MembershipList> fetchAllMembershipsWithUsers() {
		return membershipBackendClient.fetchMemberships()
				.thenCompose(members -> {
					CompletableFuture<Object> userCalls = membershipBackendClient.fetchUsers()
						.thenApply(userList -> userList.getUsers());
					return CompletableFuture.allOf(userCalls)
							.thenApply(nil -> members);
				});
	}
}
