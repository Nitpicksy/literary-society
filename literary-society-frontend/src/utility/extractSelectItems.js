export const extractSelectItems = (enumList) => {
    const selectItemsMap = new Map();
    for (let item of enumList) {
        selectItemsMap.set(item.key, item.value);
    }
    return Object.fromEntries(selectItemsMap);
}