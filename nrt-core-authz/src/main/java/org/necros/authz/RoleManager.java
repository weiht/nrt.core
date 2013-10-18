package org.necros.authz;

public interface RoleManager {
	public abstract Role get(String id);
	public abstract Role getWithName(String name);
	public abstract Role add(Role role);
	public abstract Role update(Role role);
	public abstract Role remove(String id);
	public abstract Role enable(String id);
	public abstract Role disable(String id);

	public abstract List<Role> all();
	public abstract int countAll();
	public abstract Pager<Role> pageAll(Pager<Role> page);

	public abstract List<Role> filtered(String filterText);
	public abstract int countFiltered(String filterText);
	public abstract Pager<Role> pageFiltered(String filterText, Pager<Role> page);

	public abstract RoleMember addMember(String roleId, String loginId);
	public abstract RoleMember removeMember(String roleId, String loginId);

	public abstract List<RoleMember> allMembers();
	public abstract int countAllMembers();
	public abstract Pager<RoleMember> pageAllMembers(Pager<RoleMember> page);

	public abstract ObjectPermission addPermission(String roleId, String objectId);
	public abstract ObjectPermission removePermission(String roleId, String objectId);

	public abstract List<ObjectPermission> allPermissions();
	public abstract int countAllPermissions();
	public abstract Pager<ObjectPermission> pageAllPermissions(Pager<ObjectPermission> page);
	public abstract List<ObjectPermission> allPermissionsWithType(String objectType);
	public abstract int countAllPermissionsWithType(String objectType);
	public abstract Pager<ObjectPermission> pageAllPermissionsWithType(String objectType,
		Pager<ObjectPermission> page);
}
