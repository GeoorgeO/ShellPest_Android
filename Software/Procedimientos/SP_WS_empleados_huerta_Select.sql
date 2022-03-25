USE [ShellPest]
GO
-- ================================================
-- Template generated from Template Explorer using:
-- Create Procedure (New Menu).SQL
--
-- Use the Specify Values for Template Parameters 
-- command (Ctrl-Shift-M) to fill in the parameter 
-- values below.
--
-- This block of comments will not be included in
-- the definition of the procedure.
-- ================================================
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF  EXISTS (SELECT * FROM SYS.OBJECTS WHERE TYPE = 'P' AND NAME = 'SP_WS_empleados_huerta_Select')
DROP PROCEDURE SP_WS_empleados_huerta_Select
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE SP_WS_empleados_huerta_Select
	-- Add the parameters for the stored procedure here
	@id_usuario varchar(10),
	@Fecha varchar(8)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	
		select 
	
	N.c_codigo_emp , 
	h.Id_Huerta ,
	CONCAT_WS(' ',N.v_nombre_emp,' ',N.v_apellidomat_emp, ' ',
	N.v_apellidopat_emp,' ' ) Nombre_Completo

	from GrupoAGV.dbo.nomempleados as N
	left join ShellPest.dbo.t_lugar_campo as L on L.c_codigo_lug = N.c_codigo_lug
	left join GrupoAGV.dbo.coscampo as C on C.c_codigo_cam = L.c_codigo_cam
	left join ShellPest.dbo.t_Huerta as H on H.c_codigo_cam = C.c_codigo_cam
	inner join ShellPest.dbo.t_Usuario_Huerta as U on U.Id_Huerta = H.Id_Huerta
	and U.Id_Usuario = @id_usuario

	where N.c_codigo_lug  in ('0001',
		'0002',
		'0003',
		'0004',
		'0005',
		'0006',
		'0008',
		'0010',
		'0014',
		'0015',
		'0016',
		'0017',
		'0019',
		'0021',
		'0022',
		'0027')
		and (N.d_creacion_emp >=@Fecha or N.d_modifi_emp>=@Fecha	)
	order by Nombre_Completo
		
END
GO
