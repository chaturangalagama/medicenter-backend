package com.ilt.cms.pm.integration.mapper;

import com.ilt.cms.api.entity.casem.DispatchEntity;
import com.ilt.cms.api.entity.casem.PackageEntity;
import com.ilt.cms.api.entity.casem.PackageEntity.PackageStatus;
import com.ilt.cms.core.entity.casem.Dispatch;
import com.ilt.cms.core.entity.casem.Package;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class PackageMapper extends Mapper {

    public static Package mapToCore(PackageEntity packageEntity) {
        Package aPackage = new Package();
        if (packageEntity == null) return aPackage;
        aPackage.setItemRefId(packageEntity.getItemRefId());
        aPackage.setName(packageEntity.getName());
        aPackage.setCode(packageEntity.getCode());
        aPackage.setPackageQty(packageEntity.getPackageQty());
        aPackage.setDispatches(packageEntity.getDispatches()
                .stream()
                .map(PackageMapper::mapToDispatchCore)
                .collect(Collectors.toList()));
        aPackage.setPurchasePrice(packageEntity.getPurchasePrice());
        if (PackageStatus.COMPLETED.equals(packageEntity.getStatus())) {
            aPackage.setStatus(Package.PackageStatus.COMPLETED);
        } else if (PackageStatus.ON_GOING.equals(packageEntity.getStatus())){
            aPackage.setStatus(Package.PackageStatus.ON_GOING);
        }
        return aPackage;
    }

    public static PackageEntity mapToEntity(Package aPackage) {
        PackageEntity packageEntity = new PackageEntity();
        if (aPackage == null) return packageEntity;
        packageEntity.setItemRefId(aPackage.getItemRefId());
        packageEntity.setName(aPackage.getName());
        packageEntity.setCode(aPackage.getCode());
        packageEntity.setPackageQty(aPackage.getPackageQty());
        packageEntity.setPurchaseDate(aPackage.getPurchaseDate());
        packageEntity.setExpireDate(aPackage.getExpireDate());
        packageEntity.setPurchasePrice(aPackage.getPurchasePrice());
        packageEntity.setDispatches(aPackage.getDispatches()
                .stream()
                .map(PackageMapper::mapToDispatchEntity)
                .collect(Collectors.toList()));
        if (Package.PackageStatus.COMPLETED.equals(aPackage.getStatus())) {
            packageEntity.setStatus(PackageStatus.COMPLETED);
        } else if (Package.PackageStatus.ON_GOING.equals(aPackage.getStatus())) {
            packageEntity.setStatus(PackageStatus.ON_GOING);
        }
        return packageEntity;
    }

    private static Dispatch mapToDispatchCore(DispatchEntity dispatchEntity) {
        Dispatch dispatch = new Dispatch();
        if (dispatchEntity == null) return dispatch;
        dispatch.setItemRefId(dispatchEntity.getItemRefId());
        dispatch.setItemCode(dispatchEntity.getItemCode());
        dispatch.setItemName(dispatchEntity.getItemName());
        dispatch.setUtilizedDate(LocalDateTime.now());
        dispatch.setUtilize(dispatchEntity.isUtilize());
        dispatch.setPayable(dispatchEntity.isPayable());
        return dispatch;
    }

    private static DispatchEntity mapToDispatchEntity(Dispatch dispatch) {
        DispatchEntity dispatchEntity = new DispatchEntity();
        if (dispatch == null) return dispatchEntity;
        dispatchEntity.setItemRefId(dispatch.getItemRefId());
        dispatchEntity.setPurchasedId(dispatch.getPurchasedId());
        dispatchEntity.setItemCode(dispatch.getItemCode());
        dispatchEntity.setItemName(dispatch.getItemName());
        dispatchEntity.setUtilizedDate(dispatch.getUtilizedDate());
        dispatchEntity.setUtilize(dispatch.isUtilize());
        dispatchEntity.setPayable(dispatch.isPayable());
        return dispatchEntity;
    }
}
